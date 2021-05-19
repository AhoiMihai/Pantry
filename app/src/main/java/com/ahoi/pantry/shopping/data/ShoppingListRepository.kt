package com.ahoi.pantry.shopping.data

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class ShoppingListRepository(
    private val firestore: FirebaseFirestore,
    private val pantryRefSupplier: () -> String
) {

    fun saveShoppingList(shoppingList: ShoppingList): Completable {
        val pantryRef = pantryRefSupplier()
        return Completable.create {
            firestore.collection("pantries/$pantryRef/shoppingLists")
                .document(shoppingList.name)
                .set(shoppingList.contents)
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
                    Single.just(query.documents.map { it.toObject(ShoppingList::class.java) })
                }
                .addOnFailureListener {
                    Single.error<List<ShoppingList>>(it)
                }
        }
    }

    fun deleteShoppingList(name: String): Completable {
        val pantryRef = pantryRefSupplier()
        return Completable.create {
            firestore.collection("pantries/$pantryRef/shoppingLists")
                .document(name)
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