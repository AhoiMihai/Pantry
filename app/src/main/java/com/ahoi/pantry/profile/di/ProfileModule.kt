package com.ahoi.pantry.profile.di

import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.common.rx.DefaultSchedulerProvider
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.profile.domain.InvitationRepository
import com.ahoi.pantry.profile.domain.ProfileRepository
import com.ahoi.pantry.profile.ui.MyInvitationsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @Provides
    fun provideProfileRepo(firestore: FirebaseFirestore): ProfileRepository {
        return ProfileRepository(firestore)
    }

    @Provides
    fun provideInvitationsRepo(firestore: FirebaseFirestore): InvitationRepository {
        return InvitationRepository(firestore)
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
    fun provideMyInvitationsViewModel(
        profileRepository: ProfileRepository,
        invitationRepository: InvitationRepository,
        authManager: AuthManager,
        schedulerProvider: SchedulerProvider,
        errorHandler: FirestoreErrorHandler
    ): MyInvitationsViewModel {
        return MyInvitationsViewModel(
            profileRepository,
            invitationRepository,
            authManager,
            schedulerProvider,
            errorHandler
        )
    }

}