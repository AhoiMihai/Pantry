package com.ahoi.pantry.recipes.ui

import com.ahoi.pantry.ingredients.data.model.PantryItem

class MissingIngredientsFormatter {

    fun formatRecipeIngredients(ingredients: List<PantryItem>): String {
        val builder = StringBuilder()

        ingredients.forEach {
            builder.append(it.ingredientName)
                .append(", ")
        }

        return builder.toString()
    }
}