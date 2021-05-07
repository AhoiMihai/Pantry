package com.ahoi.pantry.auth.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.ahoi.pantry.HomeActivity
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.auth.AuthMode
import com.ahoi.pantry.auth.AuthenticationViewModel
import com.ahoi.pantry.auth.OperationResult
import com.ahoi.pantry.auth.di.AuthenticationComponent
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.common.uistuff.showToast
import javax.inject.Inject

class CreateAccountActivity : PantryActivity() {

    private val emailInput: EditText by bind(R.id.email_input)
    private val passwordInput: EditText by bind(R.id.password_input)
    private val submitButton: Button by bind(R.id.submit_button)

    @Inject
    lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        (application as PantryApp).getComponent(AuthenticationComponent::class.java).inject(this)

        viewModel.operationResult.observe(this) {
            handleStateChange(it)
        }

        submitButton.setOnClickListener {
            viewModel.updateEmail(emailInput.text.toString())
            viewModel.updatePassword(passwordInput.text.toString())
            viewModel.authenticate(AuthMode.SIGN_UP)
        }

        errorHandlers[OperationResult.MISSING_CREDENTIALS] = {
            showToast(getString(R.string.missing_credentials))
        }
    }

    private fun handleStateChange(state: OperationState) {
        if (state == CommonOperationState.SUCCESS) {
            goToDashboard()
            return
        }

        handleOperationError(state)
    }

    private fun goToDashboard() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}