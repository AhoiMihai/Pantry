package com.ahoi.pantry.shopping.ui.mylists

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.HomeStyleFragment
import com.ahoi.pantry.shopping.di.ShoppingComponent
import com.ahoi.pantry.shopping.ui.listdetails.K_SELECTED_SHOPPING_LIST
import com.ahoi.pantry.shopping.ui.listdetails.ListDetailsActivity
import javax.inject.Inject

class ShoppingListFragment: HomeStyleFragment() {

    private lateinit var list: RecyclerView
    private lateinit var emptyView: TextView

    @Inject
    lateinit var adapter: ShoppingListAdapter

    @Inject
    lateinit var viewModel: ShoppingListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as PantryApp).getComponent(ShoppingComponent::class.java)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        list = view.findViewById(R.id.list)
        emptyView = view.findViewById(R.id.empty_view)

        setupList()

        viewModel.operationState.observe(this) {
            handleOperationState(it)
        }

        viewModel.shoppingLists.observe(this) {
            if (it.isEmpty()) {
                emptyView.visibility = View.VISIBLE
                return@observe
            }
            emptyView.visibility = View.GONE
            adapter.setItems(it)
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadLists()
    }

    private fun setupList() {
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(requireActivity())
        list.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))

        adapter.selectedList.subscribe {
            val intent = Intent(requireActivity(), ListDetailsActivity::class.java)
            intent.putExtra(K_SELECTED_SHOPPING_LIST, it)
            startActivity(intent)
        }
    }

    override val titleResId: Int
        get() = R.string.shopping_lists

    override fun activityFabClicked() {
        startActivity(Intent(requireActivity(), ListDetailsActivity::class.java))
    }
}