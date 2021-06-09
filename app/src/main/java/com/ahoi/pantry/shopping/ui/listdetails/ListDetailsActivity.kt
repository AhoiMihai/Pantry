package com.ahoi.pantry.shopping.ui.listdetails

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.common.uistuff.showToast
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.common.units.Unit
import com.ahoi.pantry.common.units.unitFromAbbreviation
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.ahoi.pantry.ingredients.ui.addingredient.AddIngredientActivity
import com.ahoi.pantry.ingredients.ui.addingredient.K_SELECTED_INGREDIENT
import com.ahoi.pantry.ingredients.ui.addingredient.REQUEST_CODE_ADD_INGREDIENT
import com.ahoi.pantry.shopping.data.ShoppingList
import com.ahoi.pantry.shopping.di.ShoppingComponent
import javax.inject.Inject

const val K_SELECTED_SHOPPING_LIST = "selected_shopping_list"
const val K_INGREDIENT_LIST = "ingredient_list_to_add"

@Suppress("UNCHECKED_CAST")
class ListDetailsActivity : PantryActivity() {

    private val listName: EditText by bind(R.id.editable_title)
    private val toolbar: Toolbar by bind(R.id.toolbar)
    private val ingredientList: RecyclerView by bind(R.id.ingredient_list)
    private val plusButton: ImageButton by bind(R.id.add_button)
    private val minusButton: ImageButton by bind(R.id.subtract_button)
    private val ingredientAmount: EditText by bind(R.id.ingredient_amount)
    private val unitSpinner: Spinner by bind(R.id.unit_spinner)
    private val controlContainer: LinearLayout by bind(R.id.control_container)

    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            if (unitSpinner.tag != "do not listen") {
                viewModel.selectUnit(
                    ingredientAmount.text.toString().toDouble(),
                    unitSpinner.adapter.getItem(position).toString().unitFromAbbreviation()
                )
            }
            unitSpinner.tag = "listen"
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

    }

    @Inject
    lateinit var viewModel: ShoppingListDetailsViewModel

    @Inject
    lateinit var adapter: ListItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_details)
        (application as PantryApp).getComponent(ShoppingComponent::class.java).inject(this)

        setSupportActionBar(toolbar)
        setupList()
        setupSpinner()
        setupValueEdit()
        observeLiveData()

        stateHandlers[ShoppingListState.SAVED] = { finish() }
        stateHandlers[ShoppingListState.EMPTY_LIST] =
            { showToast(getString(R.string.shopping_list_error_empty_list)) }
        stateHandlers[ShoppingListState.SHOPPING_DONE] = { finish() }
        stateHandlers[ShoppingListState.NO_UPDATES] =
            { showToast(getString(R.string.shopping_list_error_nothing_bought)) }
        stateHandlers[ShoppingListState.NO_NAME_SET] =
            { showToast(getString(R.string.shopping_list_error_no_name)) }
        stateHandlers[ShoppingListState.ZERO_QUANTITY] =
            { showToast(getString(R.string.no_buy_nothing)) }
        stateHandlers[CommonOperationState.DUPLICATE] = { showToast(getString(R.string.oh_no)) }

        val shoppingList = when {
            intent.hasExtra(K_SELECTED_SHOPPING_LIST) -> {
                intent.getParcelableExtra(K_SELECTED_SHOPPING_LIST)
            }
            intent.hasExtra(K_INGREDIENT_LIST) -> {
                ShoppingList.fromIngredientList(
                    intent.getParcelableArrayListExtra<PantryItem>(K_INGREDIENT_LIST)!!.toList()
                )
            }
            else -> {
                null
            }
        }
        viewModel.setShoppingList(shoppingList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shopping_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                viewModel.updateSelectedQuantity(
                    Quantity(
                        ingredientAmount.text.toString().toDouble(),
                        unitSpinner.selectedItem.toString().unitFromAbbreviation()
                    )
                )
                true
            }
            R.id.checkout -> {
                viewModel.finishShopping()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        viewModel.setName(listName.text.toString())
        viewModel.saveShoppingList()
    }

    private fun setupSpinner() {
        unitSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            Unit.values().map { it.abbreviation }
        )

        unitSpinner.onItemSelectedListener = spinnerListener
    }

    private fun setupValueEdit() {
        plusButton.setOnClickListener {
            ingredientAmount.setText((ingredientAmount.text.toString().toDouble() + 1).toString())
        }
        minusButton.setOnClickListener {
            ingredientAmount.setText((ingredientAmount.text.toString().toDouble() - 1).toString())
        }
    }

    private fun observeLiveData() {
        viewModel.items.observe(this) {
            adapter.setItems(it)
        }
        viewModel.listName.observe(this) {
            listName.setText(it)
        }

        viewModel.selectedItem.observe(this) {
            controlContainer.visibility = View.VISIBLE
            (unitSpinner.adapter as ArrayAdapter<String>).clear()
            (unitSpinner.adapter as ArrayAdapter<String>).addAll(
                Unit.values().filter { unit -> unit.type == it.item.unitType }
                    .map { it.abbreviation }
            )
            (unitSpinner.adapter as ArrayAdapter<String>).notifyDataSetChanged()

            unitSpinner.setSelection(it.item.quantity.unit.ordinal)
            ingredientAmount.setText(it.item.quantity.amount.toString())
        }

        viewModel.operationState.observe(this) {
            handleOperationState(it)
        }
    }

    private fun setupList() {
        ingredientList.layoutManager = LinearLayoutManager(this)
        ingredientList.adapter = adapter

        adapter.editClicked.subscribe {
            startActivityForResult(
                Intent(this, AddIngredientActivity::class.java),
                REQUEST_CODE_ADD_INGREDIENT
            )
        }

        adapter.selectedItem.subscribe {
            viewModel.selectItem(it)
        }

        adapter.shoppingEvents.subscribe {
            viewModel.purchaseItem(it.position, it.purchased)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_INGREDIENT && resultCode == RESULT_OK) {
            val newItem = data?.getParcelableExtra<PantryItem>(K_SELECTED_INGREDIENT)!!
            viewModel.addItem(newItem)
        }
    }
}