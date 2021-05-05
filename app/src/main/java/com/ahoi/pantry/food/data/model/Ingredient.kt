package com.ahoi.pantry.food.data.model

data class Ingredient(
    val name: String,
    val category: Category
)

enum class Category {
    ALCOHOLIC_BEVERAGE,
    FRUIT,
    MEAT,
    SAUCE,
    SEASONING,
    SOFT_DRINK,
    SPICE,
    VEGETABLE,
}