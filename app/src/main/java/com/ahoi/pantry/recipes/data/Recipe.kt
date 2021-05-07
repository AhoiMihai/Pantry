package com.ahoi.pantry.recipes.data

import android.os.Parcelable
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    @DocumentId
    val name: String,
    val servings: Int,
    val ingredients: List<PantryItem>,
    val steps: List<String>
) : Parcelable