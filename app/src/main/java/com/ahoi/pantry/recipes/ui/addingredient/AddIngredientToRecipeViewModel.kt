package com.ahoi.pantry.recipes.ui.addingredient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.ingredients.data.model.PantryItem

class AddIngredientToRecipeViewModel(
    private val pantry: Pantry,
    private val schedulers: SchedulerProvider,
    private val errorHandler: FirestoreErrorHandler
) {

    private val _searchResults = MutableLiveData<List<PantryItem>>()
    val searchResults: LiveData<List<PantryItem>> = _searchResults

    private val _operationResults = MutableLiveData<OperationState>()
    val operationResults: LiveData<OperationState> = _operationResults

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