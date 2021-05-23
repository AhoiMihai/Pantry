package com.ahoi.pantry.auth

import com.ahoi.pantry.auth.api.AuthManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject

class AuthManagerImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthManager {
    private val userIdSubject: BehaviorSubject<String> = BehaviorSubject.create()
    override val observeCurrentUserId: Observable<String> = userIdSubject.hide()

    override val currentUserId: String
        get() = firebaseAuth.currentUser.uid

    override fun loginWithEmailAndPassword(email: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    userIdSubject.onNext(it.user?.uid)
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun signUpWithEmailAndPassword(email: String, password: String): Single<FirebaseUser> {
        return Single.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    userIdSubject.onNext(it.user?.uid)
                    emitter.onSuccess(it.user)
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }
}