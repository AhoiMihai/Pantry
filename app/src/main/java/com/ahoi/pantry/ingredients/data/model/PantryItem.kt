package com.ahoi.pantry.ingredients.data.model

import android.os.Parcelable
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.common.units.UnitType
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class PantryItem(
    @DocumentId
    val ingredientName: String,
    val unitType: UnitType,
    val quantity: Quantity,
    val tags: List<Tag> = mutableListOf(),
) : Parcelable

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