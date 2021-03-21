package com.ahoi.pantry.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.R
import com.ahoi.pantry.common.rx.ScheduleProvider
import com.google.firebase.auth.FirebaseAuth

class CreateAccountViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val schedulers: ScheduleProvider
) {

    private val _signupState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _signupState

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

    fun createAccount() {
        val email = email.value
        val password = password.value

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            _signupState.postValue(SignUpState.MISSING_CREDENTIALS)
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _signupState.postValue(SignUpState.SUCCESS)
            }
            .addOnFailureListener {
                _signupState.postValue(SignUpState.UNKNOWN_ERROR)
            }
    }
}

enum class SignUpState(
    val success: Boolean,
    val fatal: Boolean,
    val errorStringId: Int
) {
    SUCCESS(true, false, 0),
    MISSING_CREDENTIALS(false, false, R.string.missing_credentials),
    UNKNOWN_ERROR(false, false, R.string.generic_auth_error)
}