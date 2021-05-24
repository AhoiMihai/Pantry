package com.ahoi.pantry.recipes.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.common.units.convertToBase
import com.ahoi.pantry.common.units.minus
import com.ahoi.pantry.common.units.roundToSane
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.ahoi.pantry.recipes.data.Recipe
import io.reactivex.rxjava3.core.Completable

class RecipeDetailsViewModel(
    private val pantry: Pantry,
    private val schedulers: SchedulerProvider,
    private val errorHandler: FirestoreErrorHandler
) {

    private val _operationState = MutableLiveData<OperationState>()
    val operationState: LiveData<OperationState> = _operationState

    private val _displayRecipe = MutableLiveData<Recipe>()
    val displayRecipe: LiveData<Recipe> = _displayRecipe

    fun setRecipe(recipe: Recipe?) {
        if (recipe == null) {
            _operationState.postValue(RecipeState.NO_RECIPE)
            return
        }
        _displayRecipe.postValue(recipe!!)
    }

    fun updateServings(servings: Int) {
        if (servings < 1) {
            return
        }
        _displayRecipe.postValue(displayRecipe.value?.toServings(servings))
    }

    fun makeRecipe() {
        val recipe = displayRecipe.value ?: return
        pantry.getIngredientsFromPantry(recipe.ingredients.map { it.ingredientName })
            .flatMapCompletable { ing ->
                recipe.ingredients.forEach {
                    if (it.quantity > ing[ing.indexOf(it)].quantity) {
                        return@flatMapCompletable Completable.error(NotEnoughIngredientsException())
                    }
                }
                pantry.updateOrCreateItems(
                    ing.map {
                        PantryItem(
                            it.ingredientName,
                            it.unitType,
                            it.quantity.minus(recipe.ingredients[recipe.ingredients.indexOf(it)].quantity),
                            it.tags
                        )
                    }
                )
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.mainThread())
            .subscribe({
                _operationState.postValue(CommonOperationState.SUCCESS)
            }, {
                if (it is NotEnoughIngredientsException) {
                    _operationState.postValue(RecipeState.NOT_ENOUGH_INGREDIENTS)
                } else {
                    _operationState.postValue(errorHandler.handleError(it))
                }
            })
    }
}

class NotEnoughIngredientsException : Throwable("Not enough ingredients to make recipe")

enum class RecipeState : OperationState {
    NO_RECIPE,
    NOT_ENOUGH_INGREDIENTS
}