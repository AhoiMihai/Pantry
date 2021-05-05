package com.ahoi.pantry.recipes.data

import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.food.data.model.Ingredient

data class Recipe(
    val name: String,
    val photoUrl: String,
    val ingredients: Map<Ingredient, Quantity>,
    val steps: List<String>
)