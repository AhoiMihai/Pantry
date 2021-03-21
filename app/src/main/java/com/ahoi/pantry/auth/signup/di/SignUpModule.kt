package com.ahoi.pantry.auth.signup.di

import com.ahoi.pantry.auth.signup.CreateAccountViewModel
import com.ahoi.pantry.common.rx.DefaultScheduleProvider
import com.ahoi.pantry.common.rx.ScheduleProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides

@Module
class SignUpModule {

    @Provides
    fun provideSchedulers(): ScheduleProvider {
        return DefaultScheduleProvider()
    }

    @Provides
    fun provideSignUpViewModel(
        auth: FirebaseAuth,
        schedulers: ScheduleProvider
    ): CreateAccountViewModel {
        return CreateAccountViewModel(auth, schedulers)
    }
}