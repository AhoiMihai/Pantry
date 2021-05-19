package com.ahoi.pantry.recipes.ui.details

import android.os.Bundle
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.PantryActivity

const val K_RECIPE = "recipetoshow"

class RecipeDetailsActivity : PantryActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
    }
}