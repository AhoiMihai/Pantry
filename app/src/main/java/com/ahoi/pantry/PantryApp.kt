package com.ahoi.pantry

import android.app.Application
import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.auth.di.AuthenticationComponent
import com.ahoi.pantry.auth.di.DaggerAuthenticationComponent
import com.ahoi.pantry.launch.di.DaggerLaunchComponent
import com.ahoi.pantry.launch.di.LaunchComponent
import com.ahoi.pantry.profile.di.DaggerProfileComponent
import com.ahoi.pantry.profile.di.ProfileComponent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.lang.IllegalStateException

class PantryApp : Application() {

    private val componentRegistry = HashMap<Class<out PantryComponent>, PantryComponent>()

    override fun onCreate() {
        super.onCreate()
        initComponents()
    }

    private fun initComponents() {
        componentRegistry[ProfileComponent::class.java] = DaggerProfileComponent
            .builder()
            .firestore(FirebaseFirestore.getInstance())
            .build()

        componentRegistry[AuthenticationComponent::class.java] = DaggerAuthenticationComponent
            .builder()
            .firebaseAuth(Firebase.auth)
            .build()

        componentRegistry[LaunchComponent::class.java] = DaggerLaunchComponent
            .builder()
            .firebaseAuth(Firebase.auth)
            .build()
    }

    fun <M : PantryComponent> getComponent(componentClass: Class<M>): M {
        val pantryPart = componentRegistry[componentClass]
            ?: throw IllegalStateException("Component $componentClass not registered")

        @Suppress("UNCHECKED_CAST")
        return pantryPart as M
    }
}