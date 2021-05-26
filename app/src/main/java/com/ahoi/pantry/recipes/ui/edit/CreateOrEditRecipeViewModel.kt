package com.ahoi.pantry.recipes.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.ahoi.pantry.recipes.data.Recipe
import com.ahoi.pantry.recipes.domain.RecipesRepository
import com.google.firebase.firestore.FirebaseFirestoreException

class CreateOrEditRecipeViewModel(
    private val recipesRepository: RecipesRepository,
    private val authManager: AuthManager
) {

    private val _addedIngredients = MutableLiveData<List<PantryItem>>()
    val addedIngredients: LiveData<List<PantryItem>> = _addedIngredients

    private val _recipeName = MutableLiveData<String>()
    val recipeName: LiveData<String> = _recipeName

    private val _recipeServings = MutableLiveData<Int>()
    val recipeServings: LiveData<Int> = _recipeServings

    private val _recipeSteps = MutableLiveData<List<String>>()
    val recipeSteps: LiveData<List<String>> = _recipeSteps

    private val _operationState = MutableLiveData<OperationState>()
    val operationResult: LiveData<OperationState> = _operationState

    fun addIngredients(ingredient: List<PantryItem>) {
        val ingredients = ArrayList(addedIngredients.value ?: ArrayList())
        ingredients.addAll(ingredient)
        _addedIngredients.postValue(ingredients)
    }

    fun updateRecipeName(name: String) {
        _recipeName.value = name
    }

    fun updateRecipeServings(servings: Int) {
        _recipeServings.value = servings
    }

    fun updateRecipeSteps(steps: List<String>) {
        _recipeSteps.value = steps
    }

    fun createRecipe(
        title: String,
        servings: Int,
        steps: List<String>
    ) {
        val ingredients = addedIngredients.value
        if (ingredients == null || ingredients.isEmpty()) {
            _operationState.postValue(OperationResult.EMPTY_INGREDIENTS)
            return
        }
        if (steps.isEmpty()) {
            _operationState.postValue(OperationResult.EMPTY_STEPS)
            return
        }
        val recipe = Recipe(title, servings, ingredients, steps)
        recipesRepository.createOrUpdate(authManager.currentUserId, recipe)
            .subscribe(
                {
                    _operationState.postValue(CommonOperationState.SUCCESS)
                },
                {
                    _operationState.postValue(handleRepositoryError(it))
                }
            )
    }

    private fun handleRepositoryError(throwable: Throwable): OperationState {
        return if (throwable is FirebaseFirestoreException) {
            when (throwable.code) {
                FirebaseFirestoreException.Code.INVALID_ARGUMENT -> OperationResult.VALIDATION_ERROR
                else -> CommonOperationState.UNKNOWN_ERROR
            }
        } else {
            CommonOperationState.UNKNOWN_ERROR
        }
    }
}

enum class OperationResult : OperationState {
    EMPTY_INGREDIENTS,
    EMPTY_STEPS,
    VALIDATION_ERROR,
}