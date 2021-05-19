package com.ahoi.pantry.shopping.ui.mylists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.shopping.data.ShoppingList
import com.ahoi.pantry.shopping.data.ShoppingListRepository

class ShoppingListsViewModel(
    private val repository: ShoppingListRepository,
    private val schedulers: SchedulerProvider,
    private val errorHandler: FirestoreErrorHandler
) {

    private val _shoppingLists = MutableLiveData<List<ShoppingList>>()
    val shoppingLists: LiveData<List<ShoppingList>> = _shoppingLists

    private val _operationState = MutableLiveData<OperationState>()
    val operationState: LiveData<OperationState> = _operationState

    fun loadLists() {
        repository.loadShoppingLists()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    _shoppingLists.postValue(it)
                },
                {
                    _operationState.postValue(errorHandler.handleError(it))
                }
            )
    }

}