package com.ahoi.pantry.profile.domain

import com.ahoi.pantry.profile.data.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.FirestoreClient
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class ProfileRepository(
    private val firestore: FirebaseFirestore,
) {

    private val profileSubject = BehaviorSubject.create<Profile>()
    private val profilePantrySubject: BehaviorSubject<String> = BehaviorSubject.create()
    val pantryReference: String
        get() = profilePantrySubject.value
    val currentProfile: Profile
        get() = profileSubject.value

    fun createProfile(id: String, name: String, email: String): Completable {
        return Completable.create {
            firestore.collection("pantries")
                .add(
                    mapOf(
                        "name" to "My Pantry",
                    )
                ).addOnSuccessListener {
                    val profile = Profile(
                        name = name,
                        email = email,
                        pantryReference = it.id
                    )
                    firestore
                        .collection("profiles")
                        .document(id)
                        .set(profile)
                        .addOnSuccessListener {
                            profilePantrySubject.onNext(profile.pantryReference)
                            Completable.complete()
                        }
                        .addOnFailureListener { error ->
                            Completable.error(error)
                        }
                }.addOnFailureListener {
                    Completable.error(it)
                }

        }
    }

    fun updateProfilePantryRef(id: String, reference: String): Completable {
        return Completable.create {
            firestore.collection("profiles")
                .document(id)
                .update("pantryReference", reference)
                .addOnSuccessListener {
                    Completable.complete()
                }
                .addOnFailureListener {
                    Completable.error(it)
                }
        }
    }

    fun loadProfile(id: String) {
        firestore
            .collection("profiles")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                profileSubject.onNext(document.toObject(Profile::class.java))
            }
            .addOnFailureListener { error ->
                throw error
            }
    }

    fun observeProfile(): Observable<Profile> = profileSubject.hide()
}