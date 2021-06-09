package com.ahoi.pantry.profile.domain

import com.ahoi.pantry.profile.api.ProfileRepository
import com.ahoi.pantry.profile.data.Profile
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.Optional

open class ProfileRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val userIdSupplier: () -> String
): ProfileRepository {

    private val profileSubject = BehaviorSubject.create<Optional<Profile>>()

    override fun createProfile(id: String, name: String, email: String): Completable {
        return Completable.create { emitter ->
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

    override fun updateProfilePantryRef(id: String, reference: String): Completable {
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

    override fun updateProfile(profile: Profile): Completable {
        return Completable.create { emitter ->
            firestore.collection("profiles")
                .document(userIdSupplier())
                .update(profile.toMap())
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun loadProfile(id: String): Completable {
        return Completable.create { emitter ->
            firestore
                .collection("profiles")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    profileSubject.onNext(Optional.of(document.toProfile()))
                    emitter.onComplete()
                }
                .addOnFailureListener { error ->
                    emitter.onError(error)
                }
        }
    }

    override fun getOrLoadCurrent(): Single<Profile> {
        return if (profileSubject.hasValue() && profileSubject.value.isPresent) {
            Single.just(profileSubject.value.get())
        } else {
            loadProfile(userIdSupplier())
                .toSingle { profileSubject.value.get() }
        }
    }

    override fun getOrLoadPantryReference(): Single<String> {
        return getOrLoadCurrent()
            .map { it.pantryReference }
    }

    override fun clearProfile() {
        profileSubject.onNext(Optional.empty())
    }
}

fun DocumentSnapshot.toProfile(): Profile {
    return Profile(
        this["name"] as String,
        this["email"] as String,
        this["pantryReference"] as String
    )
}

fun Profile.toMap(): Map<String, Any> {
    return mapOf(
        "name" to this.name,
        "email" to this.email,
    )
}