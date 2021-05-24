package com.ahoi.pantry.recipes.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.common.uistuff.showToast
import com.ahoi.pantry.recipes.data.Recipe
import com.ahoi.pantry.recipes.di.RecipesComponent
import com.ahoi.pantry.recipes.ui.RecipeIngredientFormatter
import com.ahoi.pantry.recipes.ui.RecipeStepsFormatter
import com.ahoi.pantry.recipes.ui.edit.CreateOrEditRecipeActivity
import com.ahoi.pantry.recipes.ui.edit.K_RECIPE_TO_EDIT
import com.ahoi.pantry.shopping.ui.listdetails.K_INGREDIENT_LIST
import com.ahoi.pantry.shopping.ui.listdetails.ListDetailsActivity
import javax.inject.Inject

const val K_RECIPE = "recipetoshow"

class RecipeDetailsActivity : PantryActivity() {

    private val toolbar: Toolbar by bind(R.id.toolbar)
    private val servingsText: TextView by bind(R.id.servings)
    private val minusButton: ImageButton by bind(R.id.minus_button)
    private val plusButton: ImageButton by bind(R.id.plus_button)
    private val ingredientsText: TextView by bind(R.id.ingredients_list)
    private val stepsText: TextView by bind(R.id.recipe_steps)
    private val makeButton: Button by bind(R.id.make_button)

    @Inject
    lateinit var viewModel: RecipeDetailsViewModel

    @Inject
    lateinit var recipeStepsFormatter: RecipeStepsFormatter

    @Inject
    lateinit var recipeIngredientFormatter: RecipeIngredientFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        (application as PantryApp).getComponent(RecipesComponent::class.java).inject(this)
        setSupportActionBar(toolbar)

        stateHandlers[RecipeState.NO_RECIPE] = {
            showToast(getString(R.string.oh_no))
            finish()
        }
        stateHandlers[RecipeState.NOT_ENOUGH_INGREDIENTS] = {
            showToast(getString(R.string.recipe_not_enough_ingredients))
        }
        stateHandlers[CommonOperationState.SUCCESS] = {
            showToast(getString(R.string.enjoy_your_meal))
            finish()
        }

        setupButtons()
        viewModel.displayRecipe.observe(this) {
            displayRecipe(it)
        }
        viewModel.operationState.observe(this) {
            handleOperationState(it)
        }
        val recipe = intent.getParcelableExtra<Recipe>(K_RECIPE)
        viewModel.setRecipe(recipe)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.generate_shopping_list -> {
                val intent = Intent(this, ListDetailsActivity::class.java)
                intent.putParcelableArrayListExtra(
                    K_INGREDIENT_LIST,
                    ArrayList(viewModel.displayRecipe.value?.ingredients)
                )
                startActivity(intent)
                true
            }
            R.id.edit -> {
                val displayRecipe = viewModel.displayRecipe.value
                val intent = Intent(this, CreateOrEditRecipeActivity::class.java)
                intent.putExtra(K_RECIPE_TO_EDIT, displayRecipe?.toServings(displayRecipe.servings))
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupButtons() {
        plusButton.setOnClickListener {
            viewModel.updateServings(servingsText.text.toString().toInt() + 1)
        }
        minusButton.setOnClickListener {
            viewModel.updateServings(servingsText.text.toString().toInt() - 1)
        }
        makeButton.setOnClickListener {
            viewModel.makeRecipe()
        }
    }

    private fun displayRecipe(recipe: Recipe) {
        toolbar.title = recipe.name
        servingsText.text = recipe.servings.toString()
        ingredientsText.text = recipeIngredientFormatter.formatRecipeIngredients(recipe.ingredients)
        stepsText.text = recipeStepsFormatter.formatRecipeSteps(recipe.steps)
    }
}