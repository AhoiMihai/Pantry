package com.ahoi.pantry.recipes.di

import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.recipes.ui.addsteps.AddStepsToRecipeActivity
import com.ahoi.pantry.recipes.ui.details.RecipeDetailsActivity
import com.ahoi.pantry.recipes.ui.edit.CreateOrEditRecipeActivity
import com.ahoi.pantry.recipes.ui.myrecipes.MyRecipesFragment
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
        fun authManager(value: AuthManager): Builder

        @BindsInstance
        fun pantry(pantry: Pantry): Builder

        fun build(): RecipesComponent
    }

    fun inject(activity: CreateOrEditRecipeActivity)
    fun inject(activity: AddStepsToRecipeActivity)
    fun inject(activity: RecipeDetailsActivity)

    fun inject(fragment: MyRecipesFragment)
}