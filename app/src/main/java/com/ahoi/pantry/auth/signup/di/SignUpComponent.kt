package com.ahoi.pantry.auth.signup.di

import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.auth.signup.CreateAccountActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.BindsInstance
import dagger.Component

@Component(modules = [SignUpModule::class])
interface SignUpComponent: PantryComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun firebaseAuth(value: FirebaseAuth): Builder

        fun build(): SignUpComponent
    }

    fun inject(activity: CreateAccountActivity)
}