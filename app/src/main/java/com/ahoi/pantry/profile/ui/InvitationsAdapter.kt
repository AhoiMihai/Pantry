package com.ahoi.pantry.profile.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.R
import com.ahoi.pantry.profile.data.Invitation
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class InvitationsAdapter(
    private val context: Context
): RecyclerView.Adapter<InvitationsAdapter.ViewHolder>() {

     val invitations: ArrayList<Invitation> = ArrayList()

    private val acceptedInvitationSubject = PublishSubject.create<Invitation>()
    val acceptedInvitation: Observable<Invitation> = acceptedInvitationSubject.hide()

    fun setItems(newInvitations: List<Invitation>) {
        invitations.clear()
        invitations.addAll(newInvitations)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        invitations.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val invitationText: TextView = view.findViewById(R.id.name_text)
        val acceptButton: ImageButton = view.findViewById(R.id.accept_invitation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.l_invitation, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.invitationText.text =
            String.format(context.getString(R.string.invitations_name_text), invitations[position].sourceDisplayName)
        holder.acceptButton.setOnClickListener {
            acceptedInvitationSubject.onNext(invitations[position])
        }
    }

    override fun getItemCount() = invitations.size
}