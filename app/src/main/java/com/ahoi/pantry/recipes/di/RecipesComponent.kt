package com.ahoi.pantry.recipes.di

import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.recipes.ui.MyRecipesActivity
import com.ahoi.pantry.recipes.ui.edit.CreateOrEditRecipeActivity
import com.google.firebase.firestore.FirebaseFirestore
import dagger.BindsInstance
import dagger.Component

@Component(modules = [RecipesModule::class])
interface RecipesComponent: PantryComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun firestore(value: FirebaseFirestore): Builder

        @BindsInstance
        fun AuthManager(value: AuthManager): Builder

        fun build(): RecipesComponent
    }

    fun inject(activity: CreateOrEditRecipeActivity)
    fun inject(activity: MyRecipesActivity)
}