package com.ahoi.pantry.auth.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.profile.data.Profile
import com.ahoi.pantry.profile.domain.ProfileRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authManager: AuthManager,
    private val schedulers: SchedulerProvider,
    private val firestoreErrorHandler: FirestoreErrorHandler
) {

    private val _operationState = MutableLiveData<OperationState>()
    val operationState: LiveData<OperationState> = _operationState

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    fun loadProfile() {
        profileRepository.getOrLoadCurrent()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe({
                _profile.postValue(it)
            }, {
                _operationState.postValue(firestoreErrorHandler.handleError(it))
            })
    }

    fun saveChanges(name: String, email: String) {
        if (name.isEmpty() || email.isEmpty()) {
            return
        }
        profileRepository.updateProfile(
            Profile(
                name,
                email,
                ""
            )
        ).subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe({
                _operationState.postValue(CommonOperationState.SUCCESS)
            }, {
                _operationState.postValue(firestoreErrorHandler.handleError(it))
            })
    }

    fun logout() {
        authManager.logout()
        _operationState.postValue(ProfileState.LOGGED_OUT)
    }
}

enum class ProfileState: OperationState {
    LOGGED_OUT
}
