package com.ahoi.pantry.recipes.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.ahoi.pantry.recipes.data.Recipe
import com.ahoi.pantry.recipes.domain.RecipesRepository
import com.google.firebase.firestore.FirebaseFirestoreException

class CreateOrEditRecipeViewModel(
    private val recipesRepository: RecipesRepository
) {

    private val _addedIngredients = MutableLiveData<List<PantryItem>>()
    val addedIngredients: LiveData<List<PantryItem>> = _addedIngredients

    private val _operationState = MutableLiveData<OperationState>()
    val operationState: LiveData<OperationState> = _operationState

    fun addIngredient(ingredient: PantryItem) {
        val ingredients = ArrayList(addedIngredients.value)
        ingredients.add(ingredient)
        _addedIngredients.postValue(ingredients)
    }

    fun createRecipe(
        title: String,
        servings: Int,
        steps: List<String>
    ) {
        val ingredients = addedIngredients.value
        if (ingredients == null || ingredients.isEmpty()) {
            _operationState.postValue(OperationState.EMPTY_INGREDIENTS)
            return
        }
        val recipe = Recipe(title, servings, ingredients, steps)
        recipesRepository.createOrUpdate(recipe)
            .subscribe(
                {
                    _operationState.postValue(OperationState.SUCCESS)
                },
                {
                    _operationState.postValue(handleRepositoryError(it))
                }
            )
    }

    private fun handleRepositoryError(throwable: Throwable): OperationState {
        return if (throwable is FirebaseFirestoreException) {
            when (throwable.code) {
                FirebaseFirestoreException.Code.INVALID_ARGUMENT -> OperationState.VALIDATION_ERROR
                else -> OperationState.UNKNOWN_ERROR
            }
        } else {
            OperationState.UNKNOWN_ERROR
        }
    }
}

enum class OperationState {
    SUCCESS,
    EMPTY_INGREDIENTS,
    VALIDATION_ERROR,
    UNKNOWN_ERROR
}