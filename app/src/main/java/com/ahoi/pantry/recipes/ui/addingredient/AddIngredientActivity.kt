package com.ahoi.pantry.recipes.ui.addingredient

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.recipes.di.RecipesComponent
import com.ahoi.pantry.recipes.ui.edit.IngredientListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

const val REQUEST_CODE_ADD_INGREDIENT = 43111
const val K_SELECTED_INGREDIENT = "pantryselectedingredient"

class AddIngredientActivity : PantryActivity() {

    private val searchView: SearchView by bind(R.id.ingredient_search)
    private val ingredientList: RecyclerView by bind(R.id.ingredient_list)
    private val fab: FloatingActionButton by bind(R.id.fab)

    private val adapter = IngredientListAdapter(false)

    @Inject
    lateinit var viewModel: AddIngredientToRecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient_to_recipe)
        (application as PantryApp).getComponent(RecipesComponent::class.java).inject(this)

        adapter.clickedItem
            .subscribe {
                val data = Intent()
                data.putExtra(K_SELECTED_INGREDIENT, it)
                setResult(RESULT_OK, data)
                finish()
            }
    }
}