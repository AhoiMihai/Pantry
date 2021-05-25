package com.ahoi.pantry.common.uistuff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.ahoi.pantry.R
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.launch.LandingActivity

abstract class PantryActivity : AppCompatActivity() {

    protected val stateHandlers: MutableMap<OperationState, () -> Unit> = mutableMapOf(
        CommonOperationState.UNAUTHORIZED to {
            showToast(getString(R.string.error_message_unauthorized))
            val intent = Intent(this, LandingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        },
        CommonOperationState.UNKNOWN_ERROR to { showToast(getString(R.string.error_message_unknown_error)) }
    )

    protected fun handleOperationState(error: OperationState) {
        if (stateHandlers[error] != null) {
            stateHandlers[error]!!()
        } else {
            stateHandlers[CommonOperationState.UNKNOWN_ERROR]!!()
        }
    }
}