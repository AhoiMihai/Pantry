package com.ahoi.pantry.launch.di

import com.ahoi.pantry.launch.LaunchViewModel
import com.ahoi.pantry.profile.domain.ProfileRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class LandingModule {

    @Provides
    fun provideLandingViewModel(
        auth: FirebaseAuth,
        profileRepository: ProfileRepositoryImpl
    ): LaunchViewModel {
        return LaunchViewModel(auth, profileRepository)
    }
}