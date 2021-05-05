package com.ahoi.pantry.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ahoi.pantry.HomeActivity
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.landing.AuthState
import com.ahoi.pantry.landing.LandingActivity
import com.ahoi.pantry.landing.LaunchViewModel
import com.ahoi.pantry.launch.di.LaunchComponent
import javax.inject.Inject

class LaunchActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: LaunchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        (application as PantryApp).getComponent(LaunchComponent::class.java).inject(this)
        lifecycle.addObserver(viewModel)

        viewModel.authState.observe(this) {
            it?.let {
                when (it) {
                    AuthState.AUTHENTICATED -> startActivity(Intent(this, HomeActivity::class.java))
                    AuthState.UNAUTHENTICATED -> startActivity(
                        Intent(
                            this,
                            LandingActivity::class.java
                        )
                    )
                }

                finish()
            }
        }
    }

}