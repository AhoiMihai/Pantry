package com.ahoi.pantry.recipes.data

import android.os.Parcelable
import com.ahoi.pantry.common.units.Quantity
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    @DocumentId
    val name: String,
    val servings: Int,
    val ingredients: Map<String, Quantity> = mutableMapOf(),
    val steps: List<String> = arrayListOf()
) : Parcelable