package com.ahoi.pantry.recipes.di

import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.recipes.domain.RecipesRepository
import com.ahoi.pantry.recipes.ui.RecipeStepsFormatter
import com.ahoi.pantry.recipes.ui.edit.CreateOrEditRecipeViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class RecipesModule {

    @Provides
    fun provideRecipeRepository(
        firestore: FirebaseFirestore,
        authManager: AuthManager
    ): RecipesRepository {
        return RecipesRepository(firestore)
    }

    @Provides
    fun provideCreateOrEditRecipeViewModel(repository: RecipesRepository, authManager: AuthManager): CreateOrEditRecipeViewModel {
        return CreateOrEditRecipeViewModel(repository, authManager)
    }

    @Provides
    fun provideRecipeStepsFormatter(): RecipeStepsFormatter {
        return RecipeStepsFormatter()
    }
}