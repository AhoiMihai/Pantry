package com.ahoi.pantry.auth.details

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.auth.di.AuthenticationComponent
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.common.uistuff.showToast
import com.ahoi.pantry.launch.LandingActivity
import com.ahoi.pantry.profile.di.ProfileComponent
import javax.inject.Inject

class ProfileActivity : PantryActivity() {

    private val emailText: EditText by bind(R.id.email_input)
    private val usernameText: EditText by bind(R.id.name_input)
    private val saveButton: Button by bind(R.id.save_button)
    private val logoutButton: Button by bind(R.id.logout_button)

    @Inject
    lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        (application as PantryApp).getComponent(AuthenticationComponent::class.java).inject(this)

        viewModel.profile.observe(this) {
            emailText.setText(it.email)
            usernameText.setText(it.name)
        }

        stateHandlers[CommonOperationState.SUCCESS] = {
            showToast(getString(R.string.profile_updated_success))
        }

        stateHandlers[ProfileState.LOGGED_OUT] = {
            showToast(getString(R.string.logged_out_successfully))
            val intent = Intent(this, LandingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        viewModel.operationState.observe(this) {
            handleOperationState(it)
        }
        saveButton.setOnClickListener {
            viewModel.saveChanges(usernameText.text.toString(), emailText.text.toString())
        }

        logoutButton.setOnClickListener { viewModel.logout() }
        viewModel.loadProfile()
    }
}