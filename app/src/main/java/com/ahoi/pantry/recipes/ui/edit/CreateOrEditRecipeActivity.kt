package com.ahoi.pantry.recipes.ui.edit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.recipes.data.Recipe
import com.ahoi.pantry.recipes.di.RecipesComponent
import com.ahoi.pantry.recipes.ui.addingredient.AddIngredientToRecipeActivity
import com.ahoi.pantry.recipes.ui.addingredient.REQUEST_CODE_ADD_INGREDIENT
import javax.inject.Inject

const val K_RECIPE_TO_EDIT = "pantryrecipetoedit"

class CreateOrEditRecipeActivity : PantryActivity(), EditClickListener {

    private val recipeTitle: EditText by bind(R.id.recipe_title)
    private val recipeServings: EditText by bind(R.id.recipe_servings)
    private val ingredientList: RecyclerView by bind(R.id.ingredient_list)
    private val recipeText: TextView by bind(R.id.recipe_steps)
    private val doneButton: Button by bind(R.id.done_button)

    private val adapter = IngredientListAdapter(true, this)

    @Inject
    lateinit var viewModel: CreateOrEditRecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
        (application as PantryApp).getComponent(RecipesComponent::class.java).inject(this)

        if (intent.hasExtra(K_RECIPE_TO_EDIT) && intent.getParcelableExtra<Recipe>(K_RECIPE_TO_EDIT) != null) {
//            viewModel.setCurrentRecipe(intent.getParcelableExtra(K_RECIPE_TO_EDIT)!!)
        }

        setUpList()
    }

    private fun setUpList() {
        ingredientList.adapter = adapter
        ingredientList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        ingredientList.layoutManager = LinearLayoutManager(this)
    }

    override fun onEditClick() {
        startActivityForResult(
            Intent(this, AddIngredientToRecipeActivity::class.java),
            REQUEST_CODE_ADD_INGREDIENT
        )
    }
}