package com.ahoi.pantry.recipes.di

import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.common.rx.DefaultSchedulerProvider
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.recipes.domain.RecipeCardsGenerator
import com.ahoi.pantry.recipes.domain.RecipesRepository
import com.ahoi.pantry.recipes.ui.RecipeStepsFormatter
import com.ahoi.pantry.recipes.ui.addsteps.AddStepsToRecipeViewModel
import com.ahoi.pantry.recipes.ui.details.RecipeDetailsViewModel
import com.ahoi.pantry.recipes.ui.edit.CreateOrEditRecipeViewModel
import com.ahoi.pantry.recipes.ui.myrecipes.MyRecipesViewModel
import com.ahoi.pantry.recipes.ui.myrecipes.RecipesAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
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
    fun provideRecipeCardsGenerator(
        authManager: AuthManager,
        recipesRepository: RecipesRepository,
        pantry: Pantry
    ): RecipeCardsGenerator {
        return RecipeCardsGenerator(authManager, recipesRepository, pantry)
    }

    @Provides
    fun provideErrorHandler(): FirestoreErrorHandler {
        return FirestoreErrorHandler()
    }

    @Provides
    fun provideSchedulers(): SchedulerProvider {
        return DefaultSchedulerProvider()
    }

    @Provides
    fun providePicasso(): Picasso {
        return Picasso.get()
    }

    @Provides
    fun provideRecipeStepsFormatter(): RecipeStepsFormatter {
        return RecipeStepsFormatter()
    }

    @Provides
    fun provideRecipesAdapter(picasso: Picasso): RecipesAdapter {
        return RecipesAdapter(picasso)
    }

    @Provides
    fun provideCreateOrEditRecipeViewModel(
        repository: RecipesRepository,
        authManager: AuthManager
    ): CreateOrEditRecipeViewModel {
        return CreateOrEditRecipeViewModel(repository, authManager)
    }


    @Provides
    fun provideRecipeStepsViewModel(): AddStepsToRecipeViewModel {
        return AddStepsToRecipeViewModel()
    }

    @Provides
    fun provideRecipeDetailsViewModel(): RecipeDetailsViewModel {
        return RecipeDetailsViewModel()
    }

    @Provides
    fun provideMyRecipesViewModel(
        recipeCardsGenerator: RecipeCardsGenerator,
        schedulerProvider: SchedulerProvider,
        errorHandler: FirestoreErrorHandler
    ): MyRecipesViewModel {
        return MyRecipesViewModel(recipeCardsGenerator, schedulerProvider, errorHandler)
    }
}