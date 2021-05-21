package com.ahoi.pantry.recipes.ui

import com.ahoi.pantry.ingredients.data.model.PantryItem
import java.lang.StringBuilder

class RecipeIngredientFormatter {

    fun formatRecipeIngredients(ingredients: List<PantryItem>): String {
        val builder = StringBuilder()

        ingredients.forEach {
            builder.append(it.ingredientName)
                .append("\n")
                .append(it.quantity.amount)
                .append(it.quantity.unit.abbreviation)
                .append("\n\n")
        }

        return builder.toString()
    }
}