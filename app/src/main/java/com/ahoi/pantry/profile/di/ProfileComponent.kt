package com.ahoi.pantry.profile.di

import com.ahoi.pantry.arch.PantryComponent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.BindsInstance
import dagger.Component

@Component
interface ProfileComponent: PantryComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun firestore(value: FirebaseFirestore): Builder

        fun build(): ProfileComponent
    }
}