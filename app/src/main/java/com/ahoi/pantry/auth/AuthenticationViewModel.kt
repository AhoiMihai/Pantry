package com.ahoi.pantry.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.R
import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.profile.domain.ProfileRepository
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel(
    private val authManager: AuthManager,
    private val profileRepo: ProfileRepository
) {

    private val _signupState = MutableLiveData<OperationResult>()
    val operationResult: LiveData<OperationResult> = _signupState

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

    fun authenticate(mode: AuthMode) {
        val email = email.value
        val password = password.value

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            _signupState.postValue(OperationResult.MISSING_CREDENTIALS)
            return
        }
        when (mode) {
            AuthMode.LOGIN -> authManager.loginWithEmailAndPassword(email, password)
                .subscribe(
                    {
                        profileRepo.loadProfile(authManager.currentUserId)
                        _signupState.postValue(OperationResult.SUCCESS)
                    },
                    {
                        _signupState.postValue(OperationResult.UNKNOWN_ERROR)
                    })
            AuthMode.SIGN_UP -> authManager.signUpWithEmailAndPassword(email, password)
                .subscribe(
                    {
                        profileRepo.createProfile(it.uid, it.email!!, it.displayName!!)
                            .subscribe { _signupState.postValue(OperationResult.SUCCESS) }
                    }, {
                        _signupState.postValue(OperationResult.UNKNOWN_ERROR)
                    }
                )
        }
    }
}

enum class OperationResult(
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