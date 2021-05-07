package com.ahoi.pantry.ingredients.api

import com.ahoi.pantry.ingredients.data.model.PantryItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface Pantry {

    fun getQuantitiesForIngredients(ingredientNames: List<String>): Single<List<PantryItem>>

    fun updateOrCreateItems(updates: List<PantryItem>): Completable

    fun searchPantryItems(query: String): Single<List<PantryItem>>

    fun getPantryItemByName(name: String): Single<PantryItem>
}