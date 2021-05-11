package com.ahoi.pantry.profile.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.common.uistuff.showToast
import com.ahoi.pantry.profile.di.ProfileComponent
import javax.inject.Inject

class CreateInvitationActivity : PantryActivity() {
    private val emailText: EditText by bind(R.id.invitee_email)
    private val sendButton: Button by bind(R.id.send_button)

    @Inject
    lateinit var viewModel: CreateInvitationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_invitation)
        (application as PantryApp).getComponent(ProfileComponent::class.java).inject(this)

        sendButton.setOnClickListener {
            viewModel.sendInvitationToEmail(emailText.text.toString())
        }

        viewModel.operationState.observe(this) {
            if (it == CommonOperationState.SUCCESS) {
                showToast(getString(R.string.create_invitation_invitation_sent))
                finish()
            } else {
                handleOperationState(it)
            }
        }
    }
}