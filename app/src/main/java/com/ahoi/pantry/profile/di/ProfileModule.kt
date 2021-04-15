package com.ahoi.pantry.profile.di

import com.ahoi.pantry.profile.domain.ProfileRepository
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

}