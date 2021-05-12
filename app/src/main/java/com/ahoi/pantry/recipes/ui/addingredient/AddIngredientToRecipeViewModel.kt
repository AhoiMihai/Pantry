package com.ahoi.pantry.recipes.ui.addingredient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.common.units.UnitType
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.ahoi.pantry.ingredients.data.model.Tag

class AddIngredientToRecipeViewModel(
    private val pantry: Pantry,
    private val schedulers: SchedulerProvider,
    private val errorHandler: FirestoreErrorHandler
) {

    private val _searchResults = MutableLiveData<List<PantryItem>>()
    val searchResults: LiveData<List<PantryItem>> = _searchResults

    private val _operationResults = MutableLiveData<OperationState>()
    val operationResults: LiveData<OperationState> = _operationResults

    private val _selectedIngredient = MutableLiveData<Ingredient>()
    val selectedIngredient: LiveData<Ingredient> = _selectedIngredient

    private val _selectedItem = MutableLiveData<PantryItem>()
    val selectedItem: LiveData<PantryItem> = _selectedItem

    fun selectIngredient(ingredient: Ingredient) {
        _selectedIngredient.value = ingredient
    }

    fun selectQuantity(quantity: Quantity, ingredient: Ingredient?) {
        if (ingredient == null) {
            _operationResults.postValue(CommonOperationState.UNKNOWN_ERROR)
            return
        }
        _selectedItem.postValue(
            PantryItem(
                ingredient.name,
                quantity.unit.type,
                quantity,
                ingredient.tags
            )
        )
    }

    fun searchIngredients(query: String) {
        pantry.searchPantryItems(query)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    _searchResults.postValue(it)
                },
                {
                    _operationResults.postValue(errorHandler.handleError(it))
                }
            )
    }
}

data class Ingredient(
    val name: String,
    val unitType: UnitType,
    val tags: List<Tag>
)