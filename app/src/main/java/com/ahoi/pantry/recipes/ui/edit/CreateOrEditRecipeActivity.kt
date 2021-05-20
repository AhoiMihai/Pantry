package com.ahoi.pantry.recipes.ui.edit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.common.uistuff.showToast
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.ahoi.pantry.recipes.data.Recipe
import com.ahoi.pantry.recipes.di.RecipesComponent
import com.ahoi.pantry.recipes.ui.RecipeStepsFormatter
import com.ahoi.pantry.ingredients.ui.addingredient.AddIngredientActivity
import com.ahoi.pantry.ingredients.ui.addingredient.K_SELECTED_INGREDIENT
import com.ahoi.pantry.ingredients.ui.addingredient.REQUEST_CODE_ADD_INGREDIENT
import com.ahoi.pantry.recipes.ui.addsteps.K_RECIPE_STEPS
import com.ahoi.pantry.recipes.ui.addsteps.REQUEST_CODE_ADD_STEPS
import javax.inject.Inject

const val K_RECIPE_TO_EDIT = "pantryrecipetoedit"

class CreateOrEditRecipeActivity : PantryActivity() {

    private val recipeTitle: EditText by bind(R.id.recipe_title)
    private val recipeServings: EditText by bind(R.id.recipe_servings)
    private val ingredientList: RecyclerView by bind(R.id.ingredient_list)
    private val recipeText: TextView by bind(R.id.recipe_steps)
    private val doneButton: Button by bind(R.id.done_button)

    private val adapter = IngredientListAdapter(true)

    @Inject
    lateinit var viewModel: CreateOrEditRecipeViewModel

    @Inject
    lateinit var recipeStepsFormatter: RecipeStepsFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
        (application as PantryApp).getComponent(RecipesComponent::class.java).inject(this)

        setUpList()
        setUpButton()
        observeLiveData()
        recipeTitle.doOnTextChanged { text, _, _, _ -> viewModel.updateRecipeName(text.toString()) }
        recipeServings.doOnTextChanged { text, _, _, _ ->
            viewModel.updateRecipeServings(
                text.toString().toInt()
            )
        }

        if (intent.hasExtra(K_RECIPE_TO_EDIT) && intent.getParcelableExtra<Recipe>(K_RECIPE_TO_EDIT) != null) {
            val recipe = intent.getParcelableExtra<Recipe>(K_RECIPE_TO_EDIT)
            recipeTitle.setText(recipe!!.name)
            recipeServings.setText(recipe.servings)
            viewModel.updateRecipeSteps(recipe.steps)
            viewModel.addIngredients(recipe.ingredients)
        }

        stateHandlers[OperationResult.EMPTY_INGREDIENTS] =
            { showToast(getString(R.string.edit_recipe_empty_ingredients_error)) }
        stateHandlers[OperationResult.EMPTY_STEPS] =
            { showToast(getString(R.string.edit_recipe_empty_steps_error)) }
        stateHandlers[OperationResult.VALIDATION_ERROR] =
            { showToast(getString(R.string.edit_recipe_validation_error)) }

        adapter.editClicked
            .subscribe {
                startActivityForResult(
                    Intent(this, AddIngredientActivity::class.java),
                    REQUEST_CODE_ADD_INGREDIENT
                )
            }
    }

    private fun setUpList() {
        ingredientList.adapter = adapter
        ingredientList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        ingredientList.layoutManager = LinearLayoutManager(this)
    }

    private fun setUpButton() {
        val recipeName = viewModel.recipeName.value ?: ""
        val recipeServings = viewModel.recipeServings.value ?: 0
        val recipeSteps = viewModel.recipeSteps.value ?: mutableListOf()
        doneButton.setOnClickListener {
            viewModel.createRecipe(
                recipeName,
                recipeServings,
                recipeSteps
            )
        }
    }

    private fun observeLiveData() {

        viewModel.addedIngredients.observe(this) {
            adapter.updateItems(it)
        }
        viewModel.recipeSteps.observe(this) {
            recipeText.text = recipeStepsFormatter.formatRecipeSteps(it)
        }

        viewModel.operationResult.observe(this) {
            if (it == CommonOperationState.SUCCESS) {
                showToast(getString(R.string.edit_recipe_operation_success))
                finish()
            } else {
                handleOperationState(it)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_INGREDIENT && resultCode == RESULT_OK) {
            data?.extras?.getParcelable<PantryItem>(K_SELECTED_INGREDIENT)?.let {
                viewModel.addIngredients(listOf(it))
            }
        }
        if (requestCode == REQUEST_CODE_ADD_STEPS && resultCode == RESULT_OK) {
            data?.extras?.getStringArrayList(K_RECIPE_STEPS)?.let {
                viewModel.updateRecipeSteps(it)
            }
        }
    }
}