package com.ahoi.pantry.recipes.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahoi.pantry.recipes.data.Recipe
import com.ahoi.pantry.recipes.domain.RecipesRepository

class CreateOrEditRecipeViewModel(
    private val recipesRepository: RecipesRepository
) {

    private val _currentRecipe = MutableLiveData<Recipe>()
    val currentRecipe: LiveData<Recipe> = _currentRecipe

    fun setCurrentRecipe(recipe: Recipe) {
        _currentRecipe.value = recipe
    }
}