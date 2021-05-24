package com.ahoi.pantry.recipes.domain

import com.ahoi.pantry.auth.api.AuthManager
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.common.units.convertTo
import com.ahoi.pantry.common.units.minus
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.ahoi.pantry.recipes.api.RecipeRepository
import com.ahoi.pantry.recipes.data.Recipe
import com.ahoi.pantry.recipes.data.RecipeCardInfo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class RecipeCardsGenerator(
    private val authManager: AuthManager,
    private val recipeRepository: RecipeRepository,
    private val pantry: Pantry
) {

    fun loadRecipeCards(
        size: Int,
        recipeNameToStartAfter: String?
    ): Observable<RecipeCardInfo> {
        return recipeRepository.loadRecipes(authManager.currentUserId, size, recipeNameToStartAfter)
            .flatMapObservable { recipes ->
                Observable.fromIterable(recipes).flatMapSingle { recipe ->
                    pantry.getIngredientsFromPantry(recipe.ingredients.map { it.ingredientName })
                        .map { pantryContents ->
                            val missingIngredients =
                                calculateMissingIngredients(recipe, pantryContents)
                            RecipeCardInfo(recipe, missingIngredients)
                        }
                }
            }

    }

    private fun calculateMissingIngredients(
        recipe: Recipe,
        ingredientsFromPantry: List<PantryItem>
    ): List<PantryItem> {
        val excludedIngredients = recipe.ingredients.filter { !ingredientsFromPantry.contains(it) }
        return recipe.ingredients.filter {
            ingredientsFromPantry.contains(it) && it.quantity > ingredientsFromPantry[ingredientsFromPantry.indexOf(
                it
            )].quantity
        }.map {
            PantryItem(
                ingredientName = it.ingredientName,
                unitType = it.unitType,
                quantity = it.quantity.minus(ingredientsFromPantry[ingredientsFromPantry.indexOf(it)].quantity),
                tags = it.tags
            )
        }.plus(excludedIngredients)
    }
}