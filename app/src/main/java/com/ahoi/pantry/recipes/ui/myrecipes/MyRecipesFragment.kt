package com.ahoi.pantry.recipes.ui.myrecipes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.HomeStyleFragment
import com.ahoi.pantry.recipes.data.RecipeCardInfo
import com.ahoi.pantry.recipes.di.RecipesComponent
import com.ahoi.pantry.recipes.ui.details.K_RECIPE
import com.ahoi.pantry.recipes.ui.details.RecipeDetailsActivity
import com.ahoi.pantry.recipes.ui.edit.CreateOrEditRecipeActivity
import com.ahoi.pantry.shopping.ui.listdetails.K_INGREDIENT_LIST
import com.ahoi.pantry.shopping.ui.listdetails.ListDetailsActivity
import javax.inject.Inject

private const val PAGE_SIZE = 25

class MyRecipesFragment : HomeStyleFragment() {

    private lateinit var recipesList: RecyclerView
    private lateinit var emptyView: TextView

    @Inject
    lateinit var viewModel: MyRecipesViewModel

    @Inject
    lateinit var adapter: RecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as PantryApp).getComponent(RecipesComponent::class.java)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        recipesList = view.findViewById(R.id.list)
        emptyView = view.findViewById(R.id.empty_view)

        setupList()

        viewModel.recipeCards.observe(this) {
            hideEmptyView()
            adapter.addRecipes(it.toList())
        }

        stateHandlers[RecipesOperationState.EMPTY_LIST] = { showEmptyView() }

        viewModel.operationState.observe(this) {
            handleOperationState(it)
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadRecipes(PAGE_SIZE)
    }

    override val titleResId: Int
        get() = R.string.recipes

    override fun activityFabClicked() {
        goToCreateRecipe()
    }

    private fun showEmptyView() {
        emptyView.visibility = View.VISIBLE
        recipesList.visibility = View.GONE
    }

    private fun setupList() {
        recipesList.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        recipesList.layoutManager = layoutManager
//        recipesList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recipesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy);
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE
                ) {
                    viewModel.loadRecipes(PAGE_SIZE)
                }
            }
        })

        adapter.selectedRecipe.subscribe {
            goToRecipeDetails(it)
        }

        adapter.shoppingList.subscribe {
            val intent = Intent(requireActivity(), ListDetailsActivity::class.java)
            intent.putParcelableArrayListExtra(K_INGREDIENT_LIST, ArrayList(it.missingIngredients))
            startActivity(intent)
        }
    }

    private fun goToRecipeDetails(info: RecipeCardInfo) {
        val intent = Intent(requireActivity(), RecipeDetailsActivity::class.java)
        intent.putExtra(K_RECIPE, info.recipe)
        startActivity(intent)
    }

    private fun goToCreateRecipe() {
        startActivity(Intent(requireActivity(), CreateOrEditRecipeActivity::class.java))
    }

    private fun hideEmptyView() {
        emptyView.visibility = View.GONE
        recipesList.visibility = View.VISIBLE
    }
}