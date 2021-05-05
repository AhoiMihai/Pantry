package com.ahoi.pantry.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.R
import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.profile.data.Profile
import com.ahoi.pantry.profile.domain.ProfileRepository
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val authManager: AuthManager,
    private val profileRepo: ProfileRepository
) {

    private val _signupState = MutableLiveData<AuthenticationState>()
    val authenticationState: LiveData<AuthenticationState> = _signupState

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePassword(password: String) {
        _password.value = password
    }

    fun createAccount(mode: AuthMode) {
        val email = email.value
        val password = password.value

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            _signupState.postValue(AuthenticationState.MISSING_CREDENTIALS)
            return
        }
        when (mode) {
            AuthMode.LOGIN -> authManager.loginWithEmailAndPassword(email, password)
                .subscribe(
                    {
                        profileRepo.loadProfile(authManager.currentUserId)
                        _signupState.postValue(AuthenticationState.SUCCESS)
                    },
                    {
                        _signupState.postValue(AuthenticationState.UNKNOWN_ERROR)
                    })
            AuthMode.SIGN_UP -> firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val profile = Profile(
                        it.user!!.email!!,
                        it.user!!.displayName!!
                    )
                    profileRepo.createProfile(it.user!!.uid, profile)
                        .subscribe { _signupState.postValue(AuthenticationState.SUCCESS) }
                }.addOnFailureListener {
                    _signupState.postValue(AuthenticationState.UNKNOWN_ERROR)
                }
        }
    }
}

enum class AuthenticationState(
    val success: Boolean,
    val fatal: Boolean,
    val errorStringId: Int
) {
    SUCCESS(true, false, 0),
    MISSING_CREDENTIALS(false, false, R.string.missing_credentials),
    UNKNOWN_ERROR(false, false, R.string.generic_auth_error)
}

enum class AuthMode {
    LOGIN,
    SIGN_UP
}