package com.ahoi.pantry.recipes.data

import android.os.Parcelable
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.food.data.model.Ingredient
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val name: String,
    val servings: Int,
    val ingredients: Map<Ingredient, Quantity> = mutableMapOf(),
    val steps: List<String> = arrayListOf()
) : Parcelable