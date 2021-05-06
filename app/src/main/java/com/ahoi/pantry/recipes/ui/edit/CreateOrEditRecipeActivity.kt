package com.ahoi.pantry.recipes.ui.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.recipes.data.Recipe
import com.ahoi.pantry.recipes.di.RecipesComponent
import javax.inject.Inject

const val K_RECIPE_TO_EDIT = "pantryrecipetoedit"

class CreateOrEditRecipeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: CreateOrEditRecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
        (application as PantryApp).getComponent(RecipesComponent::class.java).inject(this)

        if (intent.hasExtra(K_RECIPE_TO_EDIT) && intent.getParcelableExtra<Recipe>(K_RECIPE_TO_EDIT) != null) {
            viewModel.setCurrentRecipe(intent.getParcelableExtra(K_RECIPE_TO_EDIT)!!)
        }

    }

    private fun observeRecipe() {
        viewModel.currentRecipe.observe(this) {

        }
    }
}