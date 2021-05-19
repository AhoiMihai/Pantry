package com.ahoi.pantry.shopping.di

import com.ahoi.pantry.common.rx.DefaultSchedulerProvider
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.shopping.data.ShoppingListRepository
import com.ahoi.pantry.shopping.ui.listdetails.ListItemsAdapter
import com.ahoi.pantry.shopping.ui.listdetails.ShoppingListDetailsViewModel
import com.ahoi.pantry.shopping.ui.mylists.ShoppingListAdapter
import com.ahoi.pantry.shopping.ui.mylists.ShoppingListsViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides

@Module
class ShoppingModule {

    @Provides
    fun provideSchedulers(): SchedulerProvider {
        return DefaultSchedulerProvider()
    }

    @Provides
    fun provideErrorHandler(): FirestoreErrorHandler {
        return FirestoreErrorHandler()
    }

    @Provides
    fun provideShoppingListsAdapter(): ShoppingListAdapter {
        return ShoppingListAdapter()
    }

    @Provides
    fun provideShoppingListItemAdapter(): ListItemsAdapter {
        return ListItemsAdapter()
    }

    @Provides
    fun provideShoppingRepository(
        firestore: FirebaseFirestore,
        pantryRefSupplier: () -> String
    ): ShoppingListRepository {
        return ShoppingListRepository(firestore, pantryRefSupplier)
    }

    @Provides
    fun provideShoppingListDetailsViewModel(
        repository: ShoppingListRepository,
        schedulerProvider: SchedulerProvider,
        errorHandler: FirestoreErrorHandler
    ): ShoppingListDetailsViewModel {
        return ShoppingListDetailsViewModel(
            repository,
            schedulerProvider,
            errorHandler
        )
    }

    @Provides
    fun provideShoppingListsViewModel(
        repository: ShoppingListRepository,
        schedulerProvider: SchedulerProvider,
        errorHandler: FirestoreErrorHandler
    ): ShoppingListsViewModel {
        return ShoppingListsViewModel(
            repository,
            schedulerProvider,
            errorHandler
        )
    }
}