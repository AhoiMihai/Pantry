package com.ahoi.pantry.ingredients.di

import com.ahoi.pantry.common.rx.DefaultSchedulerProvider
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.ingredients.data.PantryImpl
import com.ahoi.pantry.ingredients.ui.CreateOrEditIngredientViewModel
import com.ahoi.pantry.ingredients.ui.addingredient.AddIngredientViewModel
import com.ahoi.pantry.profile.domain.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class IngredientsModule {

    @Provides
    fun providePantryRefSupplier(profileRepository: ProfileRepository): () -> String {
        return { profileRepository.pantryReference }
    }

    @Provides
    fun providePantry(firestore: FirebaseFirestore, pantryRefSupplier: () -> String): Pantry {
        return PantryImpl(pantryRefSupplier, firestore)
    }

    @Provides
    fun provideSchedulers(): SchedulerProvider {
        return DefaultSchedulerProvider()
    }

    @Provides
    fun provideErrorHandler(): FirestoreErrorHandler {
        return FirestoreErrorHandler()
    }

    @Provides
    fun provideAddIngredientViewModel(
        pantry: Pantry,
        schedulerProvider: SchedulerProvider,
        errorHandler: FirestoreErrorHandler
    ): AddIngredientViewModel {
        return AddIngredientViewModel(pantry, schedulerProvider, errorHandler)
    }

    @Provides
    fun provideCreateOrEditIngredientViewModel(
        pantry: Pantry
    ): CreateOrEditIngredientViewModel {
        return CreateOrEditIngredientViewModel(pantry)
    }
}