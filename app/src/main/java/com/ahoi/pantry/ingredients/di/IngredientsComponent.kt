package com.ahoi.pantry.ingredients.di

import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.auth.di.AuthenticationComponent
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.ingredients.ui.CreateIngredientActivity
import com.google.firebase.firestore.FirebaseFirestore
import dagger.BindsInstance
import dagger.Component

@Component(modules = [IngredientsModule::class])
interface IngredientsComponent: PantryComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun firestore(value: FirebaseFirestore): Builder

        @BindsInstance
        fun pantryRefSupplier(value: () -> String): Builder

        fun build(): IngredientsComponent
    }

    fun inject(activity: CreateIngredientActivity)

    fun pantry(): Pantry
}