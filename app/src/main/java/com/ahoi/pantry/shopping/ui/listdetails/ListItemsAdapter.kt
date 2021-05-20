package com.ahoi.pantry.shopping.ui.listdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahoi.pantry.R
import com.ahoi.pantry.shopping.data.ShoppingListItem
import com.ahoi.pantry.shopping.ui.mylists.ShoppingListAdapter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.CompletableSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class ListItemsAdapter(
    private val items: MutableList<ShoppingListItem> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val selectedItemSubject = BehaviorSubject.create<ShoppingListItem>()
    val selectedItem = selectedItemSubject.hide()

    private val shoppingEventSubject = PublishSubject.create<ShoppingEvent>()
    val shoppingEvents = shoppingEventSubject.hide()

    private val editClickedSubject = CompletableSubject.create()
    val editClicked: Completable = editClickedSubject.hide()

    class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemQuantity: TextView = view.findViewById(R.id.item_quantity)
        val purchaseBox: CheckBox = view.findViewById(R.id.done_box)
    }

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            ViewType.FOOTER.ordinal
        } else {
            ViewType.CONTENT.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (ViewType.values()[viewType]) {
            ViewType.CONTENT -> {
                val holder = ContentViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.l_shopping_list_item, parent, false)
                )
                holder.itemView.setOnClickListener {
                    selectedItemSubject.onNext(items[holder.adapterPosition])
                }
                return holder
            }
            ViewType.FOOTER -> {
                val holder = FooterViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.l_add_element_to_list, parent, false)
                )

                holder.itemView.setOnClickListener {
                    editClickedSubject.onComplete()
                }

                return holder
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (ViewType.values()[holder.itemViewType]) {
            ViewType.CONTENT -> {
                holder as ContentViewHolder
                val shoppingItem = items[position]
                holder.itemName.text = shoppingItem.item.ingredientName
                holder.itemQuantity.text =
                    String.format(
                        holder.itemView.context.getString(R.string.item_quantity),
                        shoppingItem.item.quantity.amount.toString(),
                        shoppingItem.item.quantity.unit.abbreviation
                    )

                holder.purchaseBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked != shoppingItem.purchased) {
                        shoppingEventSubject.onNext(
                            ShoppingEvent(
                                holder.adapterPosition,
                                isChecked
                            )
                        )
                    }
                }
            }
            else -> {
                // nothing really
            }
        }
    }

    override fun getItemCount() = items.size + 1

    fun setItems(newItems: List<ShoppingListItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

data class ShoppingEvent(
    val position: Int,
    val purchased: Boolean
)

enum class ViewType {
    CONTENT,
    FOOTER
}