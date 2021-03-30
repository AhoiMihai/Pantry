package com.ahoi.pantry.auth.di

import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.auth.login.LoginActivity
import com.ahoi.pantry.auth.signup.CreateAccountActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AuthenticationModule::class])
interface AuthenticationComponent: PantryComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun firebaseAuth(value: FirebaseAuth): Builder

        fun build(): AuthenticationComponent
    }

    fun inject(activity: CreateAccountActivity)
    fun inject(activity: LoginActivity)
}