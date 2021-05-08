package com.ahoi.pantry.recipes.ui.addingredient

import android.os.Bundle
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.PantryActivity

const val REQUEST_CODE_ADD_INGREDIENT = 43111

class AddIngredientToRecipeActivity : PantryActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient_to_recipe)
    }
}