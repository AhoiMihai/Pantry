package com.ahoi.pantry.ingredients.ui.addingredient

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.common.uistuff.showToast
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.ingredients.di.IngredientsComponent
import com.ahoi.pantry.ingredients.ui.CreateIngredientActivity
import com.ahoi.pantry.recipes.di.RecipesComponent
import com.ahoi.pantry.recipes.ui.edit.IngredientListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

const val REQUEST_CODE_ADD_INGREDIENT = 43111
const val K_SELECTED_INGREDIENT = "pantryselectedingredient"

private const val DIALOG_TAG = "value_picker"

class AddIngredientActivity : PantryActivity(), QuantityPickedListener {

    private val searchView: SearchView by bind(R.id.ingredient_search)
    private val ingredientList: RecyclerView by bind(R.id.ingredient_list)
    private val fab: FloatingActionButton by bind(R.id.fab)

    private val adapter = IngredientListAdapter(false)

    @Inject
    lateinit var viewModel: AddIngredientViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient_to_recipe)
        (application as PantryApp).getComponent(IngredientsComponent::class.java).inject(this)

        setUpList()
        setupSearchView()
        setupButton()

        viewModel.searchResults.observe(this) {
            adapter.updateItems(it)
        }

        viewModel.selectedIngredient.observe(this) {
            val fragment: IngredientQuantityPicker? =
                supportFragmentManager.findFragmentByTag(DIALOG_TAG) as IngredientQuantityPicker
            fragment?.dismiss()

            IngredientQuantityPicker.newInstance(it.unitType)
                .show(supportFragmentManager, DIALOG_TAG)
        }

        viewModel.selectedItem.observe(this) {
            val data = Intent()
            data.putExtra(K_SELECTED_INGREDIENT, it)
            setResult(RESULT_OK, data)
            finish()
        }
    }

    private fun setupButton() {
        fab.setOnClickListener {
            startActivity(Intent(this, CreateIngredientActivity::class.java))
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchIngredients(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // nada
                return true
            }

        })
    }

    private fun setUpList() {
        ingredientList.adapter = adapter
        ingredientList.layoutManager = LinearLayoutManager(this)
        ingredientList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.clickedItem
            .subscribe {
                viewModel.selectIngredient(Ingredient(it.ingredientName, it.unitType, it.tags))
            }
    }

    override fun quantityPicked(quantity: Quantity) {
        viewModel.selectQuantity(quantity, viewModel.selectedIngredient.value)
    }

    override fun invalidQuantity() {
        showToast(getString(R.string.add_ingredient_invalid_ammount))
    }
}