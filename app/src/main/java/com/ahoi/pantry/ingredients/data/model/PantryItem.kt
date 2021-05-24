package com.ahoi.pantry.ingredients.data.model

import android.os.Parcelable
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.common.units.Unit
import com.ahoi.pantry.common.units.UnitType
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class PantryItem(
    @DocumentId
    val ingredientName: String,
    val unitType: UnitType,
    val quantity: Quantity,
    val tags: List<Tag> = mutableListOf(),
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PantryItem

        if (ingredientName != other.ingredientName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ingredientName.hashCode()
        result = 31 * result + unitType.hashCode()
        result = 31 * result + quantity.hashCode()
        result = 31 * result + tags.hashCode()
        return result
    }

}

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