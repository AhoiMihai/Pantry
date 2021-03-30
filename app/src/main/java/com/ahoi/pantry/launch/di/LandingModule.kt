package com.ahoi.pantry.launch.di

import com.ahoi.pantry.landing.LaunchViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class LandingModule {

    @Provides
    fun provideLandingViewModel(
        auth: FirebaseAuth
    ): LaunchViewModel {
        return LaunchViewModel(auth)
    }
}