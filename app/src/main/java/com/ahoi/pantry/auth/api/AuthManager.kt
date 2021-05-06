package com.ahoi.pantry.auth.api

import com.google.firebase.auth.FirebaseUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface AuthManager {
    val currentUserId: String
    val observeCurrentUserId: Observable<String>

    fun loginWithEmailAndPassword(email: String, password: String): Completable

    fun signUpWithEmailAndPassword(email: String, password: String): Single<FirebaseUser>
}