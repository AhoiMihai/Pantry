package com.ahoi.pantry.shopping.data

import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.common.units.Unit
import com.ahoi.pantry.common.units.UnitType
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.ahoi.pantry.ingredients.data.model.Tag
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.lang.IllegalArgumentException

class ShoppingListRepository(
    private val firestore: FirebaseFirestore,
    private val pantrySingle: Single<String>,
) {

    fun saveShoppingList(shoppingList: ShoppingList): Completable {
        return pantrySingle.flatMapCompletable {
            Completable.create { emitter ->
                firestore.collection("pantries/$it/shoppingLists")
                    .add(shoppingList.toMap())
                    .addOnSuccessListener {
                        emitter.onComplete()
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
            }
        }
    }

    fun updateShoppingList(shoppingList: ShoppingList): Completable {
        if (shoppingList.id.isNullOrEmpty()) {
            return Completable.error(IllegalArgumentException("Null IDs not allowed"))
        }
        return pantrySingle.flatMapCompletable {
            Completable.create { emitter ->
                firestore.collection("pantries/$it/shoppingLists")
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
    }

    fun loadShoppingLists(): Single<List<ShoppingList>> {
        return pantrySingle.flatMap {
            Single.create { emitter ->
                firestore.collection("pantries/$it/shoppingLists")
                    .get()
                    .addOnSuccessListener { query ->
                        emitter.onSuccess(query.documents.map { it.toShoppingList() })
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
            }
        }
    }

    fun deleteShoppingList(id: String): Completable {
        return pantrySingle.flatMapCompletable {
            Completable.create { emitter ->
                firestore.collection("pantries/$it/shoppingLists")
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

fun PantryItem.toMap(): Map<String, Any> {
    return mapOf(
        "ingredientName" to this.ingredientName,
        "amount" to this.quantity.amount,
        "unitType" to this.unitType.name,
        "unit" to this.quantity.unit.name,
        "tags" to this.tags.map { it.name },
    )
}

fun Map<String, Any>.toPantryItem(): PantryItem {
    return PantryItem(
        ingredientName = this["ingredientName"] as String,
        unitType = UnitType.valueOf(this["unitType"].toString()),
        quantity = Quantity(
            this["amount"] as Double,
            Unit.valueOf(this["unit"].toString())
        ),
        tags = (this["tags"] as List<String>).map { Tag.valueOf(it) }
    )
}