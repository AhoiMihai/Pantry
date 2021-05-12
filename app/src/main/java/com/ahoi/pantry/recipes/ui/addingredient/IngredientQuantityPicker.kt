package com.ahoi.pantry.recipes.ui.addingredient

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.ahoi.pantry.R
import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.common.units.Unit
import com.ahoi.pantry.common.units.UnitType

private const val K_UNIT_TYPE = "picker_selected_ingredient"

class IngredientQuantityPicker : DialogFragment() {

    private lateinit var listener: QuantityPickedListener

    companion object {
        fun newInstance(unitType: UnitType): IngredientQuantityPicker {
            val instance = IngredientQuantityPicker()
            val arguments = Bundle()
            arguments.putString(K_UNIT_TYPE, unitType.name)
            instance.arguments = arguments
            return instance
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is QuantityPickedListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout =
            inflater.inflate(R.layout.ingredient_quantity_picker_fragment, container, false)
        val amount: EditText = layout.findViewById(R.id.ingredient_amount)
        val spinner: Spinner = layout.findViewById(R.id.unit_spinner)
        val okButton: TextView = layout.findViewById(R.id.ok_button)
        val unitType = arguments?.let {
            UnitType.valueOf(it.getString(K_UNIT_TYPE) ?: UnitType.MASS.name)
        } ?: UnitType.MASS
        setupSpinner(spinner, unitType)
        setupButton(okButton, amount, spinner)

        return layout

    }

    private fun setupButton(button: TextView, amount: EditText, spinner: Spinner) {
        button.setOnClickListener {
            if (::listener.isInitialized) {
                if (isQuantityValid(amount)) {
                    listener.quantityPicked(
                        Quantity(
                            amount.text.toString().toDouble(),
                            spinner.selectedItem as Unit
                        )
                    )
                } else {
                    listener.invalidQuantity()
                }
            }
        }
    }


    private fun setupSpinner(spinner: Spinner, filter: UnitType) {
        spinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            Unit.values().filter { it.type == filter }
        )
    }

    private fun isQuantityValid(editText: EditText): Boolean {
        return editText.text.toString().isEmpty() && editText.text.toString().toDouble() > 0
    }
}

interface QuantityPickedListener {
    fun quantityPicked(quantity: Quantity)
    fun invalidQuantity()
}