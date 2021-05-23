package com.ahoi.pantry.shopping.data

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
        return Completable.create {
            firestore.collection("pantries/$pantryRef/shoppingLists")
                .add(shoppingList)
                .addOnSuccessListener {
                    Completable.complete()
                }
                .addOnFailureListener {
                    Completable.error(it)
                }
        }
    }

    fun updateShoppingList(shoppingList: ShoppingList): Completable {
        if (shoppingList.id.isNullOrEmpty()) {
            return Completable.error(IllegalArgumentException("Null IDs not allowed"))
        }
        val pantryRef = pantryRefSupplier()
        return Completable.create {
            firestore.collection("pantries/$pantryRef/shoppingLists")
                .document(shoppingList.id)
                .set(shoppingList)
                .addOnSuccessListener {
                    Completable.complete()
                }
                .addOnFailureListener {
                    Completable.error(it)
                }
        }
    }

    fun loadShoppingLists(): Single<List<ShoppingList>> {
        val pantryRef = pantryRefSupplier()
        return Single.create {
            firestore.collection("pantries/$pantryRef/shoppingLists")
                .get()
                .addOnSuccessListener { query ->
                    query.documents.map { it.toObject(ShoppingList::class.java) }
                }
                .addOnFailureListener {
                    Single.error<List<ShoppingList>>(it)
                }
        }
    }

    fun deleteShoppingList(id: String): Completable {
        val pantryRef = pantryRefSupplier()
        return Completable.create {
            firestore.collection("pantries/$pantryRef/shoppingLists")
                .document(id)
                .delete()
                .addOnSuccessListener {
                    Completable.complete()
                }
                .addOnFailureListener {
                    Completable.error(it)
                }
        }
    }

}