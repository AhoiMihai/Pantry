package com.ahoi.pantry.shopping.ui.mylists

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import com.ahoi.pantry.shopping.di.ShoppingComponent
import com.ahoi.pantry.shopping.ui.listdetails.K_SELECTED_SHOPPING_LIST
import com.ahoi.pantry.shopping.ui.listdetails.ListDetailsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class ShoppingListsActivity : PantryActivity() {

    private val list: RecyclerView by bind(R.id.list)
    private val emptyView: TextView by bind(R.id.empty_view)
    private val fab: FloatingActionButton by bind(R.id.fab)

    @Inject
    lateinit var adapter: ShoppingListAdapter

    @Inject
    lateinit var viewModel: ShoppingListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_with_fab)
        (application as PantryApp).getComponent(ShoppingComponent::class.java).inject(this)
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

        fab.setOnClickListener {
            startActivity(Intent(this, ListDetailsActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadLists()
    }

    private fun setupList() {
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
        list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter.selectedList.subscribe {
            val intent = Intent(this, ListDetailsActivity::class.java)
            intent.putExtra(K_SELECTED_SHOPPING_LIST, it)
            startActivity(intent)
        }
    }
}