package com.ahoi.pantry.launch

import androidx.lifecycle.*
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.profile.domain.ProfileRepository
import com.google.firebase.auth.FirebaseAuth

class LaunchViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val profileRepository: ProfileRepository,
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