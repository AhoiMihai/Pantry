package com.ahoi.pantry.auth.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ahoi.pantry.R
import javax.inject.Inject

class CreateAccountActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: CreateAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
    }
}