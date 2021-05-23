package com.ahoi.pantry.shopping.data

import com.ahoi.pantry.ingredients.data.model.toMap
import com.ahoi.pantry.ingredients.data.model.toPantryItem
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.lang.IllegalArgumentException

class ShoppingListRepository(
    private val firestore: FirebaseFirestore,
    private val pantryRefSupplier: () -> String
) {

    fun saveShoppingList(shoppingList: ShoppingList): Completable {
        val pantryRef = pantryRefSupplier()
        return Completable.create { emitter ->
            firestore.collection("pantries/$pantryRef/shoppingLists")
                .add(shoppingList.toMap())
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                   emitter.onError(it)
                }
        }
    }

    fun updateShoppingList(shoppingList: ShoppingList): Completable {
        if (shoppingList.id.isNullOrEmpty()) {
            return Completable.error(IllegalArgumentException("Null IDs not allowed"))
        }
        val pantryRef = pantryRefSupplier()
        return Completable.create { emitter ->
            firestore.collection("pantries/$pantryRef/shoppingLists")
                .document(shoppingList.id)
                .set(shoppingList.toMap())
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                   emitter.onError(it)
                }
        }
    }

    fun loadShoppingLists(): Single<List<ShoppingList>> {
        val pantryRef = pantryRefSupplier()
        return Single.create { emitter ->
            firestore.collection("pantries/$pantryRef/shoppingLists")
                .get()
                .addOnSuccessListener { query ->
                    emitter.onSuccess(query.documents.map { it.toShoppingList() })
                }
                .addOnFailureListener {
                   emitter.onError(it)
                }
        }
    }

    fun deleteShoppingList(id: String): Completable {
        val pantryRef = pantryRefSupplier()
        return Completable.create { emitter ->
            firestore.collection("pantries/$pantryRef/shoppingLists")
                .document(id)
                .delete()
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                   emitter.onError(it)
                }
        }
    }
}

fun ShoppingList.toMap(): Map<String, Any> {
    return mapOf(
        "name" to this.name,
        "contents" to this.contents.toMap()
    )
}

fun List<ShoppingListItem>.toMap(): List<Map<String, Any>> {
    return this.map {
        mapOf(
            "item" to it.item.toMap(),
            "purchased" to it.purchased
        )
    }
}

fun DocumentSnapshot.toShoppingList(): ShoppingList {
    return ShoppingList(
        this.id,
        this["name"] as String,
        (this["contents"] as List<Map<String, Any>>).toShoppingListContents().toMutableList()
    )
}

fun List<Map<String, Any>>.toShoppingListContents(): List<ShoppingListItem> {
    return this.map {
        ShoppingListItem(
            (it["item"] as Map<String, Any>).toPantryItem(),
            it["purchased"] as Boolean
        )
    }
}