package com.ahoi.pantry.recipes.domain

import com.ahoi.pantry.recipes.api.RecipeRepository
import com.ahoi.pantry.recipes.data.Recipe
import com.ahoi.pantry.recipes.data.RecipeCardInfo
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class RecipesRepository(
    private val firestore: FirebaseFirestore,
) : RecipeRepository {

    override fun createOrUpdate(userId: String, recipe: Recipe): Completable {

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

    override fun loadRecipes(
        userId: String,
        numberToLoad: Int,
        idToStartFrom: String?
    ): Single<List<Recipe>> {
        return Single.create {
            firestore.collection("profiles/$userId/recipes")
                .limit(numberToLoad.toLong())
                .orderBy("name")
                .startAfter(idToStartFrom)
                .get()
                .addOnSuccessListener { recipeDocuments ->
                    Single.just(recipeDocuments.documents.map {
                        it.toObject(Recipe::class.java)
                    })
                }
                .addOnFailureListener {
                    Single.error<List<Recipe>>(it)
                }
        }
    }

    override fun loadRecipesFlowable(
        userId: String,
        numberToLoad: Int,
        idToStartFrom: String?
    ): Flowable<Recipe> {
        return Flowable.create({
            firestore.collection("profiles/$userId/recipes")
                .limit(numberToLoad.toLong())
                .orderBy("name")
                .startAfter(idToStartFrom)
                .get()
                .addOnSuccessListener { recipeDocuments ->
                    Flowable.fromIterable(recipeDocuments.documents.map {
                        it.toObject(Recipe::class.java)
                    })
                }
                .addOnFailureListener {
                    Flowable.error<Recipe>(it)
                }
        }, BackpressureStrategy.BUFFER)
    }


    override fun loadFullRecipe(userId: String, name: String): Single<Recipe> {

        return Single.create {
            firestore.collection("profiles/$userId/recipes")
                .whereEqualTo("name", name)
                .limit(1)
                .get()
                .addOnSuccessListener {
                    Single.just(it.documents[0].toObject(Recipe::class.java))
                }
                .addOnFailureListener {
                    Single.error<Recipe>(it)
                }
        }
    }
}