package com.ahoi.pantry.shopping.ui.listdetails

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.shopping.di.ShoppingComponent
import javax.inject.Inject

const val K_SELECTED_SHOPPING_LIST = "selected_shopping_list"
const val K_INGREDIENT_LIST = "ingredient_list_to_add"

class ListDetailsActivity : PantryActivity() {

    private val listName: EditText by bind(R.id.editable_title)
    private val toolbar: Toolbar by bind(R.id.toolbar)
    private val ingredientList: RecyclerView by bind(R.id.ingredient_list)
    private val plusButton: ImageButton by bind(R.id.add_button)
    private val minusButton: ImageButton by bind(R.id.subtract_button)
    private val ingredientAmount: EditText by bind(R.id.ingredient_amount)
    private val unitSpinner: Spinner by bind(R.id.unit_spinner)

    @Inject
    lateinit var viewModel: ShoppingListDetailsViewModel

    @Inject
    lateinit var adapter: ListItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_details)
        (application as PantryApp).getComponent(ShoppingComponent::class.java).inject(this)

        setSupportActionBar(toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shopping_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                viewModel.saveShoppingList()
                true
            }
            R.id.checkout -> {
                viewModel.finishShopping()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}