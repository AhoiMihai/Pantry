package com.ahoi.pantry.recipes.ui.addingredient

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahoi.pantry.R

class IngredientQuantityPicker : Fragment() {

    companion object {
        fun newInstance() = IngredientQuantityPicker()
    }

    private lateinit var viewModel: IngredientQuantityPickerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ingredient_quantity_picker_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}