package com.ahoi.pantry.profile.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.PantryApp
import com.ahoi.pantry.R
import com.ahoi.pantry.common.uistuff.PantryActivity
import com.ahoi.pantry.common.uistuff.SwipeToDeleteCallback
import com.ahoi.pantry.common.uistuff.bind
import com.ahoi.pantry.common.uistuff.showToast
import com.ahoi.pantry.profile.di.ProfileComponent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class MyInvitationsActivity : PantryActivity() {

    private val invitationList: RecyclerView by bind(R.id.invitation_list)
    private val emptyView: TextView by bind(R.id.empty_view)
    private val fab: FloatingActionButton by bind(R.id.fab)

    @Inject
    lateinit var viewModel: MyInvitationsViewModel

    @Inject
    lateinit var adapter: InvitationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_invitations)
        (application as PantryApp).getComponent(ProfileComponent::class.java).inject(this)

        setupList()
        assignStateHandlers()
        observeLiveData()
        setupFab()

        viewModel.loadInvitations()
    }

    private fun setupFab() {
        fab.setOnClickListener {
            startActivity(Intent(this, CreateInvitationActivity::class.java))
            finish()
        }
    }

    private fun assignStateHandlers() {
        stateHandlers[InvitationState.EMPTY_LIST] = { showEmptyState() }
        stateHandlers[InvitationState.INVITATION_DELETED] = {
            showToast(getString(R.string.invitations_invitation_declined))
        }
        stateHandlers[InvitationState.INVITATION_ACCEPTED] = {
            showToast(getString(R.string.invitations_invitation_accepted))
            finish()
        }
    }

    private fun observeLiveData() {
        viewModel.operationState.observe(this) {
            handleOperationState(it)
        }

        viewModel.invitations.observe(this) {
            showPopulatedState()
            adapter.setItems(it)
        }
    }

    private fun setupList() {
        invitationList.adapter = adapter
        invitationList.layoutManager = LinearLayoutManager(this)
        invitationList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteInvitation(adapter.invitations[viewHolder.adapterPosition])
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(invitationList)

        adapter.acceptedInvitation
            .subscribe {
                viewModel.acceptInvitation(it)
            }
    }

    private fun showEmptyState() {
        emptyView.visibility = View.VISIBLE
        invitationList.visibility = View.GONE
    }

    private fun showPopulatedState() {
        emptyView.visibility = View.GONE
        invitationList.visibility = View.VISIBLE
    }
}