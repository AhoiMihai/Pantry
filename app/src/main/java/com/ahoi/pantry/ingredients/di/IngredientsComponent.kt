package com.ahoi.pantry.ingredients.di

import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.ingredients.ui.CreateIngredientActivity
import com.ahoi.pantry.ingredients.ui.addingredient.AddIngredientActivity
import com.google.firebase.firestore.FirebaseFirestore
import dagger.BindsInstance
import dagger.Component
import io.reactivex.rxjava3.core.Single

@Component(modules = [IngredientsModule::class])
interface IngredientsComponent: PantryComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun firestore(value: FirebaseFirestore): Builder

        @BindsInstance
        fun pantrySingle(value: () -> Single<String>): Builder

        fun build(): IngredientsComponent
    }

    fun inject(activity: CreateIngredientActivity)
    fun inject(activity: AddIngredientActivity)

    fun pantry(): Pantry
}