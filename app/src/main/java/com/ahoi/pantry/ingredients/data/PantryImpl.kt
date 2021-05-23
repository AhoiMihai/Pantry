package com.ahoi.pantry.ingredients.data

import com.ahoi.pantry.common.units.Quantity
import com.ahoi.pantry.common.units.Unit
import com.ahoi.pantry.common.units.UnitType
import com.ahoi.pantry.ingredients.api.Pantry
import com.ahoi.pantry.ingredients.data.model.PantryItem
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class PantryImpl(
    private val pantryRefSupplier: () -> String,
    private val firestore: FirebaseFirestore
) : Pantry {

    override fun getIngredientsFromPantry(ingredientNames: List<String>): Single<List<PantryItem>> {
        return Single.create { emitter ->
            generateGetTask(ingredientNames)
                .addOnSuccessListener { querySnapshot ->
                    val result = ArrayList<PantryItem>()
                    querySnapshot.documents.map {
                        createPantryItemFromDocument(it)
                    }
                    emitter.onSuccess(result)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun updateOrCreateItems(updates: List<PantryItem>): Completable {
        val pantryRef = pantryRefSupplier()
        val writeBatch = firestore.batch()
        val collectionRef = firestore.collection("pantries/$pantryRef/contents")
        updates.forEach { item ->
            writeBatch.set(
                collectionRef.document(item.ingredientName),
                mapOf(
                    "amount" to item.quantity.amount,
                    "unitType" to item.unitType.name,
                    "unit" to item.quantity.unit.name,
                    "tags" to item.tags.map { it.name },
                    "keywords" to generateKeywordsFromNameAlsoKnownAsFirestoreIsTrash(item.ingredientName)
                )
            )
        }
        return Completable.create {
            writeBatch.commit()
                .addOnSuccessListener {
                    Completable.complete()
                }
                .addOnFailureListener {
                    Completable.error(it)
                }
        }
    }

    override fun searchPantryItems(query: String): Single<List<PantryItem>> {
        val pantryRef = pantryRefSupplier()
        return Single.create { emitter ->
            firestore.collection("pantries/$pantryRef/contents")
                .whereArrayContains("keywords", query)
                .get()
                .addOnSuccessListener { query ->
                    val result = query.documents.map {
                        createPantryItemFromDocument(it)
                    }
                    emitter.onSuccess(result)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun getPantryItemByName(name: String): Single<PantryItem> {
        val pantryRef = pantryRefSupplier()
        return Single.create { emitter ->
            firestore.collection("pantries/$pantryRef/contents")
                .document(name)
                .get()
                .addOnSuccessListener {
                    emitter.onSuccess(createPantryItemFromDocument(it))
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    private fun generateGetTask(ingredientNames: List<String>): Task<QuerySnapshot> {
        val pantryRef = pantryRefSupplier()
        return firestore.collection("pantries/$pantryRef/contents")
            .whereIn(FieldPath.documentId(), ingredientNames)
            .get()
    }

    private fun createPantryItemFromDocument(document: DocumentSnapshot): PantryItem {
        return PantryItem(
            ingredientName = document.id,
            unitType = UnitType.valueOf(document["unitType"].toString()),
            quantity = Quantity(
                document["amount"] as Double,
                Unit.valueOf(document["unit"].toString())
            )
        )
    }

    private fun generateKeywordsFromNameAlsoKnownAsFirestoreIsTrash(name: String): List<String> {
        val result = ArrayList<String>()
        for (i in 0..name.length) {
            result.add(name.subSequence(0, i).toString())
        }

        return result
    }
}