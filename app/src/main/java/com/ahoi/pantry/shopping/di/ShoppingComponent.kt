package com.ahoi.pantry.shopping.di

import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.profile.domain.ProfileRepository
import com.ahoi.pantry.shopping.ui.listdetails.ListDetailsActivity
import com.ahoi.pantry.shopping.ui.mylists.ShoppingListsActivity
import com.google.firebase.firestore.FirebaseFirestore
import dagger.BindsInstance
import dagger.Component
import io.reactivex.rxjava3.core.Single

@Component(modules = [ShoppingModule::class])
interface ShoppingComponent: PantryComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun firestore(value: FirebaseFirestore): Builder

        @BindsInstance
        fun pantry(pantry: Pantry): Builder

        @BindsInstance
        fun pantrySingle(value: () -> Single<String>): Builder

        fun build(): ShoppingComponent
    }

    fun inject(activity: ShoppingListsActivity)
    fun inject(activity: ListDetailsActivity)
}