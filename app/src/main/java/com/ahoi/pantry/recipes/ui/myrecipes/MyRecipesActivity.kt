package com.ahoi.pantry.recipes.ui.myrecipes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.recipes.data.RecipeCardInfo
import com.ahoi.pantry.recipes.di.RecipesComponent
import com.ahoi.pantry.recipes.ui.details.K_RECIPE
import com.ahoi.pantry.recipes.ui.details.RecipeDetailsActivity
import com.ahoi.pantry.recipes.ui.edit.CreateOrEditRecipeActivity
import com.ahoi.pantry.shopping.ui.listdetails.K_INGREDIENT_LIST
import com.ahoi.pantry.shopping.ui.listdetails.ListDetailsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

private const val PAGE_SIZE = 25

class MyRecipesActivity : PantryActivity() {

    private val recipesList: RecyclerView by bind(R.id.list)
    private val emptyView: TextView by bind(R.id.empty_view)
    private val fab: FloatingActionButton by bind(R.id.fab)

    @Inject
    lateinit var viewModel: MyRecipesViewModel

    @Inject
    lateinit var adapter: RecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_with_fab)
        (application as PantryApp).getComponent(RecipesComponent::class.java).inject(this)
        setupList()

        viewModel.recipeCards.observe(this) {
            hideEmptyView()
            adapter.addRecipes(it)
        }

        stateHandlers[RecipesOperationState.EMPTY_LIST] = { showEmptyView() }

        viewModel.operationState.observe(this) {
            handleOperationState(it)
        }

        viewModel.loadRecipes(PAGE_SIZE, "")

        fab.setOnClickListener {
            goToCreateRecipe()
        }
    }

    private fun showEmptyView() {
        emptyView.visibility = View.VISIBLE
        recipesList.visibility = View.GONE
    }

    private fun setupList() {
        recipesList.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        recipesList.layoutManager = layoutManager
//        recipesList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recipesList.addOnScrollListener(object: RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy);
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE
                ) {
                    viewModel.loadRecipes(PAGE_SIZE, adapter.itemAt(totalItemCount).recipe.name)
                }
            }
        })

        adapter.selectedRecipe.subscribe {
            goToRecipeDetails(it)
        }

        adapter.shoppingList.subscribe {
            val intent = Intent(this, ListDetailsActivity::class.java)
            intent.putParcelableArrayListExtra(K_INGREDIENT_LIST, ArrayList(it.missingIngredients))
            startActivity(intent)
        }
    }

    private fun goToRecipeDetails(info: RecipeCardInfo) {
        val intent = Intent(this, RecipeDetailsActivity::class.java)
        intent.putExtra(K_RECIPE, info)
        startActivity(intent)
    }

    private fun goToCreateRecipe() {
        startActivity(Intent(this, CreateOrEditRecipeActivity::class.java))
    }

    private fun hideEmptyView() {
        emptyView.visibility = View.GONE
        recipesList.visibility = View.VISIBLE
    }
}