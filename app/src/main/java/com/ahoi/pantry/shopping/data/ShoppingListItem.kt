package com.ahoi.pantry.shopping.data

import android.os.Parcelable
import com.ahoi.pantry.ingredients.data.model.PantryItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingListItem(
    val item: PantryItem,
    val purchased: Boolean
): Parcelable