package com.ahoi.pantry.profile.domain

import com.ahoi.pantry.profile.data.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class ProfileRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    fun createProfile(user: String): Completable {
        return Completable.fromCallable {
            auth.currentUser?.let {
                firestore
                    .collection("profiles")
                    .document(it.uid)
                    .set(user)
                    .addOnSuccessListener {
                        Completable.complete()
                    }
                    .addOnFailureListener { error ->
                        Completable.error(error)
                    }
            }
        }
    }

    fun getOrLoadProfile(): Single<Profile> {
        return Single.fromCallable {
            auth.currentUser?.let {
                firestore
                    .collection(it.uid)
                    .document("profile")
                    .get()
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener { error ->
                        Completable.error(error)
                    }
            }
        }
    }
}