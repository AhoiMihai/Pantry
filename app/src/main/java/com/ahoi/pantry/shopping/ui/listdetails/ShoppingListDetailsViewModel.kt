package com.ahoi.pantry.shopping.ui.listdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.ahoi.pantry.shopping.data.ShoppingList
import com.ahoi.pantry.shopping.data.ShoppingListItem
import com.ahoi.pantry.shopping.data.ShoppingListRepository

class ShoppingListDetailsViewModel(
    private val repository: ShoppingListRepository,
    private val pantry: Pantry,
    private val schedulers: SchedulerProvider,
    private val errorHandler: FirestoreErrorHandler
) {
    private val _operationState = MutableLiveData<OperationState>()
    val operationState: LiveData<OperationState> = _operationState

    private val _listName = MutableLiveData<String>()
    val listName: LiveData<String> = _listName

    private val _items = MutableLiveData<List<ShoppingListItem>>()
    val items: LiveData<List<ShoppingListItem>> = _items

    private val _selectedItem = MutableLiveData<ShoppingListItem>()
    val selectedItem: LiveData<ShoppingListItem> = _selectedItem

    private val _updatedItem = MutableLiveData<ShoppingListItem>()
    val updatedItem: LiveData<ShoppingListItem> = _updatedItem

    fun setShoppingList(shoppingList: ShoppingList) {
        _listName.postValue(shoppingList.name)
        _items.postValue(shoppingList.contents)
    }

    fun finishShopping() {
        val shoppingList = items.value ?: return
        val name = listName.value ?: return
        val updates = shoppingList.filter { it.purchased }.map { it.item }
        if (updates.isEmpty()) {
            _operationState.postValue(ShoppingListState.NO_UPDATES)
            return
        }
        pantry.updateOrCreateItems(updates)
            .andThen(repository.deleteShoppingList(name))
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe({
                _operationState.postValue(ShoppingListState.SHOPPING_DONE)
            }, {
                _operationState.postValue(errorHandler.handleError(it))
            })

    }

    fun saveShoppingList() {
        val name = listName.value
        val shoppingItems = items.value

        if (name.isNullOrEmpty()) {
            _operationState
        }
    }

    fun updateSelectedQuantity(quantity: Quantity) {
        val selection = selectedItem.value ?: return
        if (quantity.amount == 0.0) {
            _operationState.postValue(ShoppingListState.ZERO_QUANTITY)
            return
        }
        _updatedItem.postValue(
            ShoppingListItem(
                item = PantryItem(
                    selection.item.ingredientName,
                    selection.item.unitType,
                    quantity,
                    selection.item.tags
                ),
                purchased = selection.purchased
            )
        )
    }

}

enum class ShoppingListState : OperationState {
    NO_NAME_SET,
    DUPLICATE_NAME,
    ZERO_QUANTITY,
    EMPTY_LIST,
    NO_UPDATES,
    SHOPPING_DONE,
    SAVED,
}