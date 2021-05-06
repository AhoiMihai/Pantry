package com.ahoi.pantry.food.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    val name: String,
    val category: Category
) : Parcelable

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