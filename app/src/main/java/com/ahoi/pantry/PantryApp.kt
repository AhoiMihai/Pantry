package com.ahoi.pantry

import android.app.Application
import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.auth.signup.di.DaggerSignUpComponent
import com.ahoi.pantry.auth.signup.di.SignUpComponent
import java.lang.IllegalStateException

class PantryApp : Application() {

    private val componentRegistry = HashMap<Class<out PantryComponent>, PantryComponent>()

    override fun onCreate() {
        super.onCreate()
        initComponents()
    }

    private fun initComponents() {
        componentRegistry[SignUpComponent::class.java] = DaggerSignUpComponent.builder().build()
    }

    fun <M : PantryComponent> getComponent(componentClass: Class<M>): M {
        val pantryPart = componentRegistry[componentClass]
            ?: throw IllegalStateException("Component $componentClass not registered")

        @Suppress("UNCHECKED_CAST")
        return pantryPart as M
    }
}