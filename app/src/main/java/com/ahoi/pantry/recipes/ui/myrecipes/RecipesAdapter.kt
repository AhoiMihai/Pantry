package com.ahoi.pantry.recipes.ui.myrecipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.R
import com.ahoi.pantry.recipes.data.RecipeCardInfo
import com.ahoi.pantry.recipes.ui.MissingIngredientsFormatter
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.subjects.PublishSubject

class RecipesAdapter(
    private val picasso: Picasso,
    private val formatter: MissingIngredientsFormatter,
    private val recipeList: MutableList<RecipeCardInfo> = mutableListOf(),
) : RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {

    private val shoppingListSubject = PublishSubject.create<RecipeCardInfo>()
    val shoppingList = shoppingListSubject.hide()

    private val selectedRecipeSubject = PublishSubject.create<RecipeCardInfo>()
    val selectedRecipe = selectedRecipeSubject.hide()

    fun addRecipes(recipes: List<RecipeCardInfo>) {
        recipeList.clear()
        recipeList.addAll(recipes)
        notifyDataSetChanged()
    }

    fun itemAt(position: Int): RecipeCardInfo {
        return recipeList[position]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.recipe_name)
        val contextButton: ImageButton = view.findViewById(R.id.context_button)
        val tag: View = view.findViewById(R.id.tag)
        val missingIngredientsText: TextView = view.findViewById(R.id.missing_ingredients)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.l_recipe, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recipeList[position]
        holder.nameText.text = item.recipe.name
        holder.nameText.setOnClickListener {
            selectedRecipeSubject.onNext(item)
        }
        if (!item.canMake) {
            Picasso.get()
            picasso.load(R.drawable.twotone_add_shopping_cart_24)
                .error(R.drawable.twotone_add_shopping_cart_24).fit().into(holder.contextButton)
            holder.contextButton.setOnClickListener {
                shoppingListSubject.onNext(item)
            }
            holder.tag.setBackgroundColor(holder.itemView.context.getColor(R.color.dark_grey))
            holder.missingIngredientsText.text = String.format(
                holder.itemView.context.getString(R.string.recipe_card_missing_prefix),
                formatter.formatRecipeIngredients(item.missingIngredients)
            )
        } else {
            picasso.load(R.drawable.twotone_check_circle_outline_24)
                .error(R.drawable.twotone_check_circle_outline_24).fit().into(holder.contextButton)
            holder.contextButton.setOnClickListener(null)
            holder.tag.setBackgroundColor(holder.itemView.context.getColor(R.color.teal_200))
            holder.missingIngredientsText.text = holder.itemView.context.getString(R.string.recipe_card_can_make)
        }

    }

    override fun getItemCount() = recipeList.size
}