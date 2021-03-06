package com.ahoi.pantry.recipes.domain

import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.common.units.Unit
import com.ahoi.pantry.common.units.UnitType
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.ahoi.pantry.ingredients.data.model.Tag
import com.ahoi.pantry.recipes.api.RecipeRepository
import com.ahoi.pantry.recipes.data.Recipe
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class RecipesRepository(
    private val firestore: FirebaseFirestore,
) : RecipeRepository {

    override fun createOrUpdate(userId: String, recipe: Recipe): Completable {
        return Completable.create { emitter ->
            firestore.collection("profiles/$userId/recipes")
                .document(recipe.name)
                .set(recipe.toMap())
                .addOnSuccessListener {
                    emitter.onComplete()
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun loadRecipes(
        userId: String,
        numberToLoad: Int,
        idToStartFrom: String?
    ): Single<List<Recipe>> {
        return if (idToStartFrom == null) {
            loadRecipes(userId, numberToLoad)
        } else {
            loadRecipesWithStartingPoint(userId, numberToLoad, idToStartFrom)
        }
    }

    private fun loadRecipes(
        userId: String,
        numberToLoad: Int,
    ): Single<List<Recipe>> {
        return Single.create { emitter ->
            firestore.collection("profiles/$userId/recipes")
                .orderBy(FieldPath.documentId())
                .limit(numberToLoad.toLong())
                .get()
                .addOnSuccessListener { recipeDocuments ->
                    emitter.onSuccess(recipeDocuments.documents.map {
                        it.toRecipe()
                    }.sortedBy { it.name })
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    private fun loadRecipesWithStartingPoint(
        userId: String,
        numberToLoad: Int,
        idToStartFrom: String
    ): Single<List<Recipe>> {
        return Single.create { emitter ->
            firestore.collection("profiles/$userId/recipes")
                .orderBy(FieldPath.documentId())
                .startAfter(idToStartFrom)
                .limit(numberToLoad.toLong())
                .get()
                .addOnSuccessListener { recipeDocuments ->
                    val result = recipeDocuments.documents.map {
                        it.toRecipe()
                    }.sortedBy { it.name }
                    emitter.onSuccess(result)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }
}

fun DocumentSnapshot.toRecipe(): Recipe {
    return Recipe(
        this.id,
        (this["servings"] as Long).toInt(),
        (this["ingredients"] as List<Map<String, Any>>).toPantryItems(),
        this["steps"] as List<String>
    )
}

fun List<Map<String, Any>>.toPantryItems(): List<PantryItem> {
    return this.map {
        PantryItem(
            it["ingredientName"] as String,
            UnitType.valueOf(it["unitType"] as String),
            Quantity(
                it["amount"] as Double,
                Unit.valueOf(it["unit"] as String)
            ),
            (it["tags"] as List<String>).map { Tag.valueOf(it) }
        )
    }
}

fun List<PantryItem>.toStoreEntries(): List<Map<String, Any>> {
    return this.map {
        mapOf(
            "ingredientName" to it.ingredientName,
            "unitType" to it.unitType.name,
            "amount" to it.quantity.amount,
            "unit" to it.quantity.unit.name,
            "tags" to it.tags.map { tag -> tag.name }
        )
    }
}

fun Recipe.toMap(): Map<String, Any> {
    return mapOf(
        "servings" to this.servings,
        "ingredients" to this.ingredients.toStoreEntries(),
        "steps" to this.steps
    )
}