package com.ahoi.pantry.recipes.ui.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.R
import com.ahoi.pantry.ingredients.data.model.PantryItem

class IngredientListAdapter(
    private val editable: Boolean,
    private val editClickListener: EditClickListener,
    private val ingredients: ArrayList<PantryItem> = arrayListOf(),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateItems(newItems: ArrayList<PantryItem>) {
        ingredients.clear()
        ingredients.addAll(newItems)
        notifyDataSetChanged()
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.ingredient_name)
        val quantityText: TextView = view.findViewById(R.id.ingredient_amount)
        val unitText: TextView = view.findViewById(R.id.ingredient_unit)
    }

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount && editable) {
            ViewType.FOOTER.ordinal
        } else {
            ViewType.CONTENT.ordinal
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.CONTENT -> ItemViewHolder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.l_recipe_ingredient, viewGroup, false))
            ViewType.FOOTER -> FooterViewHolder(LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.l_add_element_to_list, viewGroup, false))
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (ViewType.values()[viewHolder.itemViewType]) {
            ViewType.CONTENT -> {
                viewHolder as ItemViewHolder
                val ingredient = ingredients[position]
                viewHolder.nameText.text = ingredient.ingredientName
                viewHolder.quantityText.text = ingredient.quantity.amount.toString()
                viewHolder.unitText.text = ingredient.quantity.unit.name
            }
            ViewType.FOOTER -> {
                viewHolder.itemView.setOnClickListener {
                    editClickListener.onEditClick()
                }
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = if (editable) ingredients.size + 1 else ingredients.size
}

interface EditClickListener {
    fun onEditClick()
}

enum class ViewType {
    CONTENT,
    FOOTER
}

