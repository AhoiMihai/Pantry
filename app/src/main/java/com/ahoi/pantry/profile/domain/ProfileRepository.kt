package com.ahoi.pantry.profile.domain

import com.ahoi.pantry.profile.data.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
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
    private lateinit var _currentProfile: Profile
    private lateinit var currentPantryRefference: String
    val pantryReference: String
        get() = _currentProfile.pantryReference
    val currentProfile: Profile
        get() = _currentProfile

    fun createProfile(id: String, name: String?, email: String): Completable {
        return Completable.create { emitter ->
            firestore.collection("pantries")
                .add(
                    mapOf(
                        "name" to "My Pantry",
                    )
                ).addOnSuccessListener {
                    val profile = Profile(
                        name = name ?: "jeffrey",
                        email = email,
                        pantryReference = it.id
                    )
                    firestore
                        .collection("profiles")
                        .document(id)
                        .set(profile)
                        .addOnSuccessListener {
                            profilePantrySubject.onNext(profile.pantryReference)
                            emitter.onComplete()
                        }
                        .addOnFailureListener { error ->
                            emitter.onError(error)
                        }
                }.addOnFailureListener {
                    emitter.onError(it)
                }

        }
    }

    fun updateProfilePantryRef(id: String, reference: String): Completable {
        return Completable.create { emitter ->
            firestore.collection("profiles")
                .document(id)
                .update("pantryReference", reference)
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    fun loadProfile(id: String): Completable {
        return Completable.create { emitter ->
            firestore
                .collection("profiles")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    _currentProfile = document.toProfile()
                    emitter.onComplete()
                }
                .addOnFailureListener { error ->
                    emitter.onError(error)
                }
        }
    }
}

fun DocumentSnapshot.toProfile(): Profile {
    return Profile(
        this["name"] as String,
        this["email"] as String,
        this["pantryReference"] as String
    )
}