package com.ahoi.pantry.shopping.data

import android.os.Parcelable
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingList (
    @DocumentId
    val name: String,
    val contents: MutableList<ShoppingListItem>
) : Parcelable {

    companion object {

        fun fromIngredientList(list: List<PantryItem>): ShoppingList {
            return ShoppingList(
                "",
                list.map { ShoppingListItem(it, false) }.toMutableList()
            )
        }
    }
}
