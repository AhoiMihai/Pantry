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