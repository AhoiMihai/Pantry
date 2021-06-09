package com.ahoi.pantry.auth.di

import com.ahoi.pantry.auth.AuthManagerImpl
import com.ahoi.pantry.auth.AuthenticationViewModel
import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.common.rx.DefaultSchedulerProvider
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.profile.domain.ProfileRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule {

    @Provides
    fun provideAuthManager(auth: FirebaseAuth): AuthManager {
        return AuthManagerImpl(auth)
    }

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider {
        return DefaultSchedulerProvider()
    }

    @Provides
    fun provideFirestoreErrorHandler(): FirestoreErrorHandler {
        return FirestoreErrorHandler()
    }

    @Provides
    fun provideAuthenticationViewModel(
        authManager: AuthManager,
        schedulerProvider: SchedulerProvider,
        profileRepository: ProfileRepositoryImpl
    ): AuthenticationViewModel {
        return AuthenticationViewModel(authManager, schedulerProvider, profileRepository)
    }
}