package com.ahoi.pantry.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.profile.api.ProfileRepository

class AuthenticationViewModel(
    private val authManager: AuthManager,
    private val schedulers: SchedulerProvider,
    private val profileRepo: ProfileRepository
) {

    private val _signupState = MutableLiveData<OperationState>()
    val operationResult: LiveData<OperationState> = _signupState

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun authenticate(mode: AuthMode) {
        val email = email.value
        val password = password.value
        val userName = username.value

        if (email.isNullOrEmpty() || password.isNullOrEmpty() || (mode == AuthMode.SIGN_UP && userName.isNullOrEmpty())) {
            _signupState.postValue(OperationResult.MISSING_CREDENTIALS)
            return
        }
        when (mode) {
            AuthMode.LOGIN -> authManager.loginWithEmailAndPassword(email, password)
                .flatMapCompletable { profileRepo.loadProfile(authManager.currentUserId) }
                .observeOn(schedulers.io())
                .subscribeOn(schedulers.mainThread())
                .subscribe(
                    {
                        _signupState.postValue(CommonOperationState.SUCCESS)
                    }, {
                        _signupState.postValue(CommonOperationState.UNKNOWN_ERROR)
                    })
            AuthMode.SIGN_UP -> authManager.signUpWithEmailAndPassword(email, password)
                .flatMapCompletable {
                    profileRepo.createProfile(it, userName!!, email)
                        .andThen(profileRepo.loadProfile(it))
                }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribe(
                    {
                        _signupState.postValue(CommonOperationState.SUCCESS)
                    }, {
                        _signupState.postValue(CommonOperationState.UNKNOWN_ERROR)
                    }
                )
        }
    }

    fun updateUserName(username: String) {
        _username.value = username
    }
}

enum class OperationResult : OperationState {
    MISSING_CREDENTIALS,
}

enum class AuthMode {
    LOGIN,
    SIGN_UP
}