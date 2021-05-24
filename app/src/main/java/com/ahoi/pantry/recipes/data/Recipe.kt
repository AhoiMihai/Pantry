package com.ahoi.pantry.recipes.data

import android.os.Parcelable
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.common.units.roundToSane
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val name: String,
    val servings: Int,
    val ingredients: List<PantryItem>,
    val steps: List<String>
) : Parcelable {

    fun toServings(newServings: Int): Recipe {
        val adjustedIngredients = ingredients.map {
            PantryItem(
                it.ingredientName,
                it.unitType,
                Quantity(
                    ((it.quantity.amount / servings) * newServings).roundToSane(),
                    it.quantity.unit
                ),
                it.tags
            )
        }

        return Recipe(
            name,
            newServings,
            adjustedIngredients,
            steps
        )
    }

    fun ingredientsForServings(newServings: Int): List<PantryItem> {
        return ingredients.map {
            PantryItem(
                it.ingredientName,
                it.unitType,
                Quantity(
                    ((it.quantity.amount / servings) * newServings).roundToSane(),
                    it.quantity.unit
                ),
                it.tags
            )
        }
    }
}