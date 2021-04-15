package com.ahoi.pantry.launch.di

import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.launch.LaunchActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.BindsInstance
import dagger.Component

@Component(modules = [LandingModule::class])
interface LaunchComponent: PantryComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun firebaseAuth(value: FirebaseAuth): Builder

        fun build(): LaunchComponent
    }

    fun inject(activity: LaunchActivity)
}