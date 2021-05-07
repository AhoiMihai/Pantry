package com.ahoi.pantry.ingredients.data.model

import com.ahoi.pantry.common.units.Quantity
import com.google.firebase.firestore.DocumentId

data class PantryItem(
    @DocumentId
    val ingredientName: String,
    val quantity: Quantity,
    val tags: List<Tag> = mutableListOf(),
)

enum class Tag {
    ALCOHOLIC_BEVERAGE,
    FRUIT,
    MEAT,
    SAUCE,
    SEASONING,
    SOFT_DRINK,
    SPICE,
    VEGETABLE,
}