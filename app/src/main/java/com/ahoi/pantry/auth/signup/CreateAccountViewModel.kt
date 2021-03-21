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

    fun createAccount(email: String, password: String) {
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
    success: Boolean,
    fatal: Boolean,
    errorStringId: Int
) {
    SUCCESS(true, false, 0),
    UNKNOWN_ERROR(false, false, R.string.generic_auth_error)
}