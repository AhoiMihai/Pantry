package com.ahoi.pantry.recipes.ui.myrecipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.recipes.data.RecipeCardInfo
import com.ahoi.pantry.recipes.domain.RecipeCardsGenerator

class MyRecipesViewModel(
    private val recipeCardsGenerator: RecipeCardsGenerator,
    private val schedulers: SchedulerProvider,
    private val errorHandler: FirestoreErrorHandler
) {
    private val _recipeCards = MutableLiveData<Set<RecipeCardInfo>>()
    val recipeCards: LiveData<Set<RecipeCardInfo>> = _recipeCards

    private val _operationState = MutableLiveData<OperationState>()
    val operationState: LiveData<OperationState> = _operationState

    private var loading: Boolean = false

    fun loadRecipes(pageSize: Int) {
        if (loading) {
            return
        }
        loading = true
        val recipes = recipeCards.value ?: listOf()
        val lastRecipeId = if (recipes.isEmpty()) null else recipes.toList()[recipes.size - 1].recipe.name
        recipeCardsGenerator.loadRecipeCards(pageSize, lastRecipeId)
            .toList()
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.mainThread())
            .subscribe({
                _recipeCards.value?.let { cards ->
                    _recipeCards.postValue(cards.plus(it.sortedBy { card -> card.recipe.name.toUpperCase()  }))
                } ?: if (it.isEmpty()) {
                    _operationState.postValue(RecipesOperationState.EMPTY_LIST)
                } else {
                    _recipeCards.postValue(it.toSet())
                }
                loading = false
            }, {
                loading = false
                _operationState.postValue(errorHandler.handleError(it))
            })
    }

    fun clearRecipes() {
        _recipeCards.value = HashSet<RecipeCardInfo>()
    }

}

enum class RecipesOperationState : OperationState {
    EMPTY_LIST
}