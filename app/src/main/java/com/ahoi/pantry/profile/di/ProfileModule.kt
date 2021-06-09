package com.ahoi.pantry.profile.di

import android.content.Context
import com.ahoi.pantry.common.rx.DefaultSchedulerProvider
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.profile.domain.InvitationRepository
import com.ahoi.pantry.profile.domain.ProfileRepositoryImpl
import com.ahoi.pantry.profile.ui.CreateInvitationViewModel
import com.ahoi.pantry.profile.ui.InvitationsAdapter
import com.ahoi.pantry.profile.ui.MyInvitationsViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ProfileModule {

    @Provides
    @Singleton
    fun provideProfileRepo(
        firestore: FirebaseFirestore,
        userIdSupplier: () -> String
    ): ProfileRepositoryImpl {
        return ProfileRepositoryImpl(firestore, userIdSupplier)
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
    fun provideInvitationsAdapter(context: Context): InvitationsAdapter {
        return InvitationsAdapter(context)
    }

    @Provides
    fun provideMyInvitationsViewModel(
        profileRepository: ProfileRepositoryImpl,
        invitationRepository: InvitationRepository,
        supplier: () -> String,
        schedulerProvider: SchedulerProvider,
        errorHandler: FirestoreErrorHandler
    ): MyInvitationsViewModel {
        return MyInvitationsViewModel(
            profileRepository,
            invitationRepository,
            supplier,
            schedulerProvider,
            errorHandler
        )
    }

    @Provides
    fun provideCreateInvitationViewModel(
        invitationRepository: InvitationRepository,
        profileRepository: ProfileRepositoryImpl,
        schedulerProvider: SchedulerProvider,
        errorHandler: FirestoreErrorHandler
    ): CreateInvitationViewModel {
        return CreateInvitationViewModel(
            invitationRepository,
            profileRepository,
            schedulerProvider,
            errorHandler
        )
    }

}