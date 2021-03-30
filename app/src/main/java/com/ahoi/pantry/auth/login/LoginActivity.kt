package com.ahoi.pantry.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ahoi.pantry.MainActivity
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.auth.di.AuthenticationComponent
import com.ahoi.pantry.auth.signup.AuthMode
import com.ahoi.pantry.auth.signup.AuthenticationViewModel
import com.ahoi.pantry.auth.signup.SignUpState
import com.ahoi.pantry.common.uistuff.bind
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    private val emailInput: EditText by bind(R.id.email_input)
    private val passwordInput: EditText by bind(R.id.password_input)
    private val submitButton: Button by bind(R.id.submit_button)

    @Inject
    lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        (application as PantryApp).getComponent(AuthenticationComponent::class.java).inject(this)

        viewModel.signUpState.observe(this) {
            handleStateChange(it)
        }

        submitButton.setOnClickListener {
            viewModel.updateEmail(emailInput.text.toString())
            viewModel.updatePassword(passwordInput.text.toString())
            viewModel.createAccount(AuthMode.LOGIN)
        }
    }

    private fun handleStateChange(state: SignUpState) {
        if (state.success) {
            goToDashboard()
            return
        }

        Toast.makeText(this, state.errorStringId, Toast.LENGTH_SHORT).show()
    }

    private fun goToDashboard() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}