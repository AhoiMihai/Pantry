package com.ahoi.pantry.shopping.ui.mylists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.shopping.data.ShoppingList
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class ShoppingListAdapter(
    private val items: ArrayList<ShoppingList> = ArrayList()
) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    private val selectedListSubject = PublishSubject.create<ShoppingList>()
    val selectedList: Observable<ShoppingList> = selectedListSubject.hide()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(android.R.id.text1)
    }

    fun setItems(newItems: List<ShoppingList>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        )
        holder.itemView.setOnClickListener {
            selectedListSubject.onNext(items[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameText.text = items[position].name

    }

    override fun getItemCount() = items.size
}