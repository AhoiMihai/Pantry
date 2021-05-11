package com.ahoi.pantry.recipes.domain

import com.ahoi.pantry.recipes.data.Recipe
import com.ahoi.pantry.recipes.data.RecipeCardInfo
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class RecipesRepository(
    private val firestore: FirebaseFirestore,
) {

    fun createOrUpdate(userId: String, recipe: Recipe): Completable {

        return Completable.create {
            firestore.collection("profiles/$userId/recipes")
                .document(recipe.name)
                .set(recipe)
                .addOnSuccessListener {
                    Completable.complete()
                }.addOnFailureListener {
                    Completable.error(it)
                }
        }
    }

    fun loadRecipeCards(userId: String): Single<List<RecipeCardInfo>> {

        return Single.create {
            firestore.collection("profiles/$userId/recipes")
                .get()
                .addOnSuccessListener { recipeDocuments ->
                    Single.just(recipeDocuments.documents.map {
                        RecipeCardInfo(
                            it["name"] as String,
                            it["photoUrl"] as String
                        )
                    })
                }
                .addOnFailureListener {
                    Single.error<List<RecipeCardInfo>>(it)
                }
        }
    }

    fun loadFullRecipe(userId: String, name: String): Single<Recipe> {

        return Single.create {
            firestore.document("profiles/$userId/recipes/$name")
                .get()
                .addOnSuccessListener {
                    Single.just(it.toObject(Recipe::class.java))
                }
                .addOnFailureListener {
                    Single.error<Recipe>(it)
                }
        }
    }
}