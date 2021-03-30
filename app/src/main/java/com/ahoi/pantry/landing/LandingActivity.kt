package com.ahoi.pantry.landing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ahoi.pantry.R
import com.ahoi.pantry.auth.login.LoginActivity
import com.ahoi.pantry.auth.signup.CreateAccountActivity
import com.ahoi.pantry.common.uistuff.bind

class LandingActivity : AppCompatActivity() {

    val loginButton: Button by bind(R.id.login_button)
    val signUpButton: Button by bind(R.id.signup_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signUpButton.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }
    }
}