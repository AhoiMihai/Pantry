package com.ahoi.pantry.profile.di

import android.content.Context
import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.profile.domain.ProfileRepository
import com.ahoi.pantry.profile.ui.CreateInvitationActivity
import com.ahoi.pantry.profile.ui.MyInvitationsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ProfileModule::class])
interface ProfileComponent: PantryComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun firestore(value: FirebaseFirestore): Builder

        @BindsInstance
        fun userIdSupplier(value: () -> String): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): ProfileComponent
    }

    fun inject(activity: MyInvitationsActivity)
    fun inject(activity: CreateInvitationActivity)

    fun profileRepository(): ProfileRepository
}