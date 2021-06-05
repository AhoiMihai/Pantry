package com.ahoi.pantry.recipes.data

import android.os.Parcelable
import com.ahoi.pantry.common.units.convertToBase
import com.ahoi.pantry.ingredients.data.model.PantryItem
import kotlinx.parcelize.Parcelize

const val MIN_DIFFERENCE_PERCENT = 10

@Parcelize
data class RecipeCardInfo(
    val recipe: Recipe,
    val missingIngredients: List<PantryItem> = mutableListOf()
) : Parcelable {

    val canMake: Boolean
        get() = missingIngredients.isEmpty() || missingIngredients.none {
            it.quantity.convertToBase() > (recipe.ingredients[recipe.ingredients.indexOf(it)].quantity.convertToBase() * MIN_DIFFERENCE_PERCENT) / 100
        }
}
