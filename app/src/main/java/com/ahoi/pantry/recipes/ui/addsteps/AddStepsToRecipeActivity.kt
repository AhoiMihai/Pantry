package com.ahoi.pantry.recipes.ui.addsteps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ahoi.pantry.R

const val REQUEST_CODE_ADD_STEPS = 23115
const val K_RECIPE_STEPS = "addedrecipesteps"
class AddStepsToRecipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_steps_to_recipe)
    }
}