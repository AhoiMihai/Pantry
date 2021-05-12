package com.ahoi.pantry.recipes.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.common.operation.OperationState
import com.ahoi.pantry.common.rx.SchedulerProvider
import com.ahoi.pantry.common.uistuff.FirestoreErrorHandler
import com.ahoi.pantry.recipes.data.Recipe
import com.ahoi.pantry.recipes.data.RecipeCardInfo
import com.ahoi.pantry.recipes.domain.RecipesRepository

class MyRecipesViewModel(
    private val authManager: AuthManager,
    private val recipeRepository: RecipesRepository,
    private val schedulers: SchedulerProvider,
    private val errorHandler: FirestoreErrorHandler
) {
    private val _recipeCards = MutableLiveData<RecipeCardInfo>()
    val recipeCards: LiveData<RecipeCardInfo> = _recipeCards

    private val _selectedRecipe = MutableLiveData<Recipe>()
    val selectedRecipe: LiveData<Recipe> = _selectedRecipe

}

enum class RecipesOperationState: OperationState {
    EMPTY_LIST
}