package com.ahoi.pantry.auth.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ahoi.pantry.MainActivity
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.auth.signup.di.SignUpComponent
import com.ahoi.pantry.common.uistuff.bind
import javax.inject.Inject

class CreateAccountActivity : AppCompatActivity() {

    private val emailInput: EditText by bind(R.id.email_input)
    private val passwordInput: EditText by bind(R.id.password_input)
    private val submitButton: Button by bind(R.id.submit_button)

    @Inject
    lateinit var viewModel: CreateAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        (application as PantryApp).getComponent(SignUpComponent::class.java).inject(this)

        viewModel.signUpState.observe(this) {
            handleStateChange(it)
        }

        submitButton.setOnClickListener {
            viewModel.updateEmail(emailInput.text.toString())
            viewModel.updatePassword(passwordInput.text.toString())
            viewModel.createAccount()
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