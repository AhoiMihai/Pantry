package com.ahoi.pantry.auth

import com.ahoi.pantry.auth.api.AuthManager
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.core.Single

class AuthManagerImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthManager {

    override val currentUserId: String
        get() = firebaseAuth.currentUser.uid

    override fun loginWithEmailAndPassword(email: String, password: String): Single<String> {
        return Single.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    emitter.onSuccess(it.user!!.uid)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun signUpWithEmailAndPassword(email: String, password: String): Single<String> {
        return Single.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    emitter.onSuccess(it.user?.uid)
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}

