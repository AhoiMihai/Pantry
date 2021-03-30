package com.ahoi.pantry.auth.di

import com.ahoi.pantry.auth.signup.AuthenticationViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule {

    @Provides
    fun provideAuthenticationViewModel(
        auth: FirebaseAuth
    ): AuthenticationViewModel {
        return AuthenticationViewModel(auth)
    }
}