package com.ahoi.pantry.recipes.ui.addsteps

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.recipes.di.RecipesComponent
import com.ahoi.pantry.recipes.ui.RecipeStepsFormatter
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject

const val REQUEST_CODE_ADD_STEPS = 23115
const val K_RECIPE_STEPS = "addedrecipesteps"

class AddStepsToRecipeActivity : PantryActivity() {

    private val addedSteps: TextView by bind(R.id.steps)
    private val textEdit: TextInputEditText by bind(R.id.step_input)
    private val addButton: ImageButton by bind(R.id.add_button)
    private val removeButton: ImageButton by bind(R.id.remove_button)
    private val toolbar: Toolbar by bind(R.id.toolbar)

    @Inject
    lateinit var viewModel: AddStepsToRecipeViewModel

    @Inject
    lateinit var stepsFormatter: RecipeStepsFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_steps_to_recipe)
        (application as PantryApp).getComponent(RecipesComponent::class.java).inject(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addButton.setOnClickListener {
            viewModel.addStep(textEdit.text.toString())
            textEdit.setText("")
        }
        removeButton.setOnClickListener {
            viewModel.removeStep()
        }

        viewModel.steps.observe(this) {
            addedSteps.text = stepsFormatter.formatRecipeSteps(it)
        }

        if (intent.hasExtra(K_RECIPE_STEPS)) {
            viewModel.addSteps(intent.getStringArrayListExtra(K_RECIPE_STEPS)!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_steps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.done) {
            val data = Intent()
            data.putStringArrayListExtra(K_RECIPE_STEPS, ArrayList(viewModel.steps.value))
            setResult(RESULT_OK, data)
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    }
}