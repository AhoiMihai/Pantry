package com.ahoi.pantry.shopping.ui.listdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.common.units.Unit
import com.ahoi.pantry.common.units.convertTo
import com.ahoi.pantry.common.units.plus
import com.ahoi.pantry.common.units.roundToSane
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

    private var shoppingListId: String? = null

    fun setShoppingList(shoppingList: ShoppingList?) {
        if (shoppingList == null) {
            return
        }
        _listName.postValue(shoppingList.name)
        _items.postValue(shoppingList.contents)
        shoppingListId = shoppingList.id
    }

    fun setName(name: String) {
        _listName.value = name
    }

    fun finishShopping() {
        val shoppingList = items.value ?: return
        val updates = shoppingList.filter { it.purchased }.map { it.item }
        if (updates.isEmpty()) {
            _operationState.postValue(ShoppingListState.NO_UPDATES)
            return
        }

        pantry.getIngredientsFromPantry(updates.map { it.ingredientName })
            .flatMapCompletable { ing ->
                val newIngredients = updates.filter {
                    !ing.contains(it)
                }
                pantry.updateOrCreateItems(
                    ing.map {
                        PantryItem(
                            it.ingredientName,
                            it.unitType,
                            it.quantity.plus(updates[updates.indexOf(it)].quantity),
                            it.tags
                        )
                    }.plus(newIngredients)
                ).andThen(repository.deleteShoppingList(shoppingListId ?: ""))
            }.subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe({
                _operationState.postValue(ShoppingListState.SHOPPING_DONE)
            }, {
                _operationState.postValue(errorHandler.handleError(it))
            })

    }

    fun selectItem(item: ShoppingListItem) {
        _selectedItem.postValue(item)
    }

    fun purchaseItem(position: Int, purchased: Boolean) {
        val shoppingList = items.value?.toMutableList() ?: return

        val selection = shoppingList[position]
        shoppingList[position] = ShoppingListItem(
            item = PantryItem(
                selection.item.ingredientName,
                selection.item.unitType,
                selection.item.quantity,
                selection.item.tags
            ),
            purchased = purchased
        )

        _items.postValue(shoppingList)
    }

    fun selectUnit(value: Double, unit: Unit) {
        val selection = selectedItem.value ?: return

        val newValue: Double = if (unit.type != selection.item.unitType) {
            0.0
        } else {
            Quantity(value, selection.item.quantity.unit).convertTo(unit).roundToSane()
        }

        _selectedItem.postValue(
            ShoppingListItem(
                PantryItem(
                    selection.item.ingredientName,
                    unit.type,
                    Quantity(newValue, unit),
                    selection.item.tags
                ), selection.purchased
            )
        )

    }

    fun addItem(pantryItem: PantryItem) {
        val shoppingList = items.value?.toMutableList() ?: mutableListOf()

        shoppingList.add(
            ShoppingListItem(
                pantryItem,
                false
            )
        )
        _items.postValue(shoppingList)
    }

    fun saveShoppingList() {
        val name = _listName.value
        val shoppingItems = items.value

        if (name.isNullOrEmpty()) {
            _operationState.postValue(ShoppingListState.NO_NAME_SET)
            return
        }

        if (shoppingItems == null || shoppingItems.isEmpty()) {
            _operationState.postValue(ShoppingListState.EMPTY_LIST)
            return
        }

        val shoppingList = ShoppingList(
            shoppingListId,
            name,
            shoppingItems.toMutableList()
        )

        if (shoppingListId == null) {
            createNewShoppingList(shoppingList)
        } else {
            updateShoppingList(shoppingList)
        }
    }

    private fun createNewShoppingList(shoppingList: ShoppingList) {
        repository.saveShoppingList(shoppingList)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe({
                _operationState.postValue(ShoppingListState.SAVED)
            }, {
                _operationState.postValue(errorHandler.handleError(it))
            })
    }

    private fun updateShoppingList(shoppingList: ShoppingList) {
        repository.updateShoppingList(shoppingList)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe({
                _operationState.postValue(ShoppingListState.SAVED)
            }, {
                _operationState.postValue(errorHandler.handleError(it))
            })
    }

    fun updateSelectedQuantity(quantity: Quantity) {
        val selection = selectedItem.value ?: return
        if (quantity.amount == 0.0) {
            _operationState.postValue(ShoppingListState.ZERO_QUANTITY)
            return
        }
        if (items.value == null) {
            _operationState.postValue(CommonOperationState.UNKNOWN_ERROR)
            return
        }
        val newList = items.value!!.toMutableList()
        val update = ShoppingListItem(
            item = PantryItem(
                selection.item.ingredientName,
                selection.item.unitType,
                quantity,
                selection.item.tags
            ),
            purchased = selection.purchased
        )
        newList[newList.indexOf(selection)] = update
        _items.postValue(newList)
    }

}

enum class ShoppingListState : OperationState {
    NO_NAME_SET,
    ZERO_QUANTITY,
    EMPTY_LIST,
    NO_UPDATES,
    SHOPPING_DONE,
    SAVED,
}