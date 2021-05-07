package com.ahoi.pantry.ingredients.di

import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.ingredients.data.PantryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class IngredientsModule {

    @Provides
    fun providePantry(firestore: FirebaseFirestore, pantryRefSupplier: () -> String): Pantry {
        return PantryImpl(pantryRefSupplier, firestore)
    }
}