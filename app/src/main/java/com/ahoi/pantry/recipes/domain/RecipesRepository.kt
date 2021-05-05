package com.ahoi.pantry.recipes.domain

import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.recipes.data.Recipe
import com.ahoi.pantry.recipes.data.RecipeCardInfo
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class RecipesRepository(
    private val firestore: FirebaseFirestore,
    private val authManager: AuthManager,
) {

    fun createOrUpdate(recipe: Recipe): Completable {
        val id = authManager.currentUserId

        return Completable.create {
            firestore.collection("profiles/$id/recipes")
                .document(recipe.name)
                .set(recipe)
                .addOnSuccessListener {
                    Completable.complete()
                }.addOnFailureListener {
                    Completable.error(it)
                }
        }
    }

    fun loadRecipeCards(): Single<List<RecipeCardInfo>> {
        val id = authManager.currentUserId

        return Single.create {
            firestore.collection("profiles/$id/recipes")
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

    fun loadFullRecipe(name: String): Single<Recipe> {
        val id = authManager.currentUserId

        return Single.create {
            firestore.document("profiles/$id/recipes/$name")
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