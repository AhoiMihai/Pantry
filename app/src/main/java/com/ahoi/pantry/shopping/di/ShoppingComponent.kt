package com.ahoi.pantry.shopping.di

import com.ahoi.pantry.arch.PantryComponent
import com.ahoi.pantry.shopping.ui.listdetails.ListDetailsActivity
import com.ahoi.pantry.shopping.ui.mylists.ShoppingListsActivity
import com.google.firebase.firestore.FirebaseFirestore
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ShoppingModule::class])
interface ShoppingComponent: PantryComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun firestore(value: FirebaseFirestore): Builder

        @BindsInstance
        fun pantryRefSupplier(value: () -> String): Builder

        fun build(): ShoppingComponent
    }

    fun inject(activity: ShoppingListsActivity)
    fun inject(activity: ListDetailsActivity)
}