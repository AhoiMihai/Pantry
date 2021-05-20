package com.ahoi.pantry.recipes.ui.myrecipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.recipes.data.RecipeCardInfo
import com.ahoi.pantry.recipes.domain.RecipeCardsGenerator
import com.ahoi.pantry.shopping.data.ShoppingList

class MyRecipesViewModel(
    private val recipeCardsGenerator: RecipeCardsGenerator,
    private val schedulers: SchedulerProvider,
    private val errorHandler: FirestoreErrorHandler
) {
    private val _recipeCards = MutableLiveData<List<RecipeCardInfo>>()
    val recipeCards: LiveData<List<RecipeCardInfo>> = _recipeCards

    private val _operationState = MutableLiveData<OperationState>()
    val operationState: LiveData<OperationState> = _operationState

    private var loading: Boolean = false

    fun loadRecipes(pageSize: Int, startingPoint: String?) {
        if (loading) {
            return
        }
        loading = true
        recipeCardsGenerator.loadRecipeCards(pageSize, startingPoint)
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.mainThread())
            .subscribe({
                _recipeCards.value?.let { cards ->
                    _recipeCards.postValue(cards.plus(it))
                }?: if (it.isEmpty()) {
                    _operationState.postValue(RecipesOperationState.EMPTY_LIST)
                } else {
                    _recipeCards.postValue(it)
                }
                loading = false
            }, {
                loading = false
                _operationState.postValue(errorHandler.handleError(it))
            })
    }

}

enum class RecipesOperationState : OperationState {
    EMPTY_LIST
}