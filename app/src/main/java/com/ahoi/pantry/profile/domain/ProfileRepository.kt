package com.ahoi.pantry.profile.domain

import com.ahoi.pantry.profile.data.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

    fun createProfile(id: String, user: Profile): Completable {
        return Completable.create {
            firestore
                .collection("profiles")
                .document(id)
                .set(user)
                .addOnSuccessListener {
                    Completable.complete()
                }
                .addOnFailureListener { error ->
                    Completable.error(error)
                }
        }
    }

    fun createProfile(id: String, name: String, email: String): Completable {
        return Completable.create {
            firestore.collection("pantries")
                .add(
                    mapOf(
                        "name" to "My Pantry"
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

    fun loadProfile(id: String) {
        firestore
            .collection("profiles")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                profileSubject.onNext(document.toObject(Profile::class.java))
            }
            .addOnFailureListener { error ->
                Completable.error(error)
            }
    }

    fun observeProfile(): Observable<Profile> = profileSubject.hide()
}