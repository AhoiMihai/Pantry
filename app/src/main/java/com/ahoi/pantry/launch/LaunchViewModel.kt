package com.ahoi.pantry.launch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.ahoi.pantry.profile.domain.ProfileRepositoryImpl
import com.google.firebase.auth.FirebaseAuth

class LaunchViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val profileRepository: ProfileRepositoryImpl,
): LifecycleObserver {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun checkForUser() {
        if (firebaseAuth.currentUser == null) {
            _authState.postValue(AuthState.UNAUTHENTICATED)
        } else {
            profileRepository.loadProfile(firebaseAuth.currentUser.uid)
                .subscribe {
                    _authState.postValue(AuthState.AUTHENTICATED)
                }
        }
    }
}
enum class AuthState {
    AUTHENTICATED,
    UNAUTHENTICATED
}