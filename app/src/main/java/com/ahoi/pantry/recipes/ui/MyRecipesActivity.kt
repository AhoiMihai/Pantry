package com.ahoi.pantry.recipes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.PantryActivity

class MyRecipesActivity : PantryActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_recipes)
    }
}