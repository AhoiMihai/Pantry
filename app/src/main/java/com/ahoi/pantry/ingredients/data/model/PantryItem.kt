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

fun PantryItem.toMap(): Map<String, Any> {
    return mapOf(
        "ingredientName" to this.ingredientName,
        "amount" to this.quantity.amount,
        "unitType" to this.unitType.name,
        "unit" to this.quantity.unit.name,
        "tags" to this.tags.map { it.name },
    )
}

fun Map<String, Any>.toPantryItem(): PantryItem {
    return PantryItem(
        ingredientName = this["ingredientName"] as String,
        unitType = UnitType.valueOf(this["unitType"].toString()),
        quantity = Quantity(
            this["amount"] as Double,
            Unit.valueOf(this["unit"].toString())
        ),
        tags = (this["tags"] as List<String>).map { Tag.valueOf(it) }
    )
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