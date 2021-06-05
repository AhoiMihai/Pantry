package com.ahoi.pantry.ingredients.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.operation.CommonOperationState
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.common.uistuff.showToast
import com.ahoi.pantry.common.units.Unit
import com.ahoi.pantry.common.units.unitFromAbbreviation
import com.ahoi.pantry.ingredients.di.IngredientsComponent
import javax.inject.Inject

class CreateIngredientActivity : PantryActivity() {

    private val title: EditText by bind(R.id.ingredient_name)
    private val amount: EditText by bind(R.id.quantity_amount)
    private val unitSpinner: Spinner by bind(R.id.unit_spinner)
    private val doneButton: Button by bind(R.id.done_button)

    @Inject
    lateinit var viewModel: CreateOrEditIngredientViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_ingredient)
        (application as PantryApp).getComponent(IngredientsComponent::class.java).inject(this)
        lifecycle.addObserver(viewModel)

        setupButton()
        setupSpinner()

        stateHandlers[CreateIngredientState.VALIDATION_ERROR] = { showToast(getString(R.string.create_ingredient_validation_error)) }
        viewModel.createItemState.observe(this) {
            when (it) {
                CommonOperationState.SUCCESS -> showToast(getString(R.string.create_ingredient_success))
                else -> handleOperationState(it)
            }
        }
    }

    private fun setupButton() {
        doneButton.setOnClickListener {
            viewModel.createItem(
                DisplayIngredient(
                    title.text.toString(),
                    amount.text.toString().toDouble(),
                    (unitSpinner.selectedItem as String).unitFromAbbreviation()
                )
            )
        }
    }

    private fun setupSpinner() {
        unitSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            Unit.values().map { it.abbreviation }
        )
    }
}