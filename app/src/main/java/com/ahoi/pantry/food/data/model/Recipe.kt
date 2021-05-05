package com.ahoi.pantry.food.data.model

import com.ahoi.pantry.common.units.Quantity

data class Recipe(
    val ingredients: Map<Ingredient, Quantity>,
    val steps: List<String>
)