package com.ahoi.pantry.recipes.data

import com.ahoi.pantry.ingredients.data.model.PantryItem

data class RecipeCardInfo(
    val recipe: Recipe,
    val missingIngredients: List<PantryItem> = mutableListOf()
)
