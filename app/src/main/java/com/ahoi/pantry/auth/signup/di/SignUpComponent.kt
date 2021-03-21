package com.ahoi.pantry.auth.signup.di

import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.auth.signup.CreateAccountActivity
import dagger.Component

@Component(modules = [SignUpModule::class])
interface SignUpComponent: PantryComponent {

    fun inject(activity: CreateAccountActivity)
}