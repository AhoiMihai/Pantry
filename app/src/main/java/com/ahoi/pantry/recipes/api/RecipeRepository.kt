package com.ahoi.pantry.recipes.api

import com.ahoi.pantry.recipes.data.Recipe
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface RecipeRepository {

    fun createOrUpdate(userId: String, recipe: Recipe): Completable

    fun loadRecipes(userId: String, numberToLoad: Int, idToStartFrom: String?): Single<List<Recipe>>

    fun loadRecipesFlowable(userId: String, numberToLoad: Int, idToStartFrom: String?): Flowable<Recipe>

    fun loadFullRecipe(userId: String, name: String): Single<Recipe>
}