package com.ahoi.pantry.landing

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth

class LaunchViewModel(
    private val firebaseAuth: FirebaseAuth
): LifecycleObserver {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun checkForUser() {
        if (firebaseAuth.currentUser == null) {
            _authState.postValue(AuthState.UNAUTHENTICATED)
        } else {
            _authState.postValue(AuthState.AUTHENTICATED)
        }
    }
}
enum class AuthState {
    AUTHENTICATED,
    UNAUTHENTICATED
}