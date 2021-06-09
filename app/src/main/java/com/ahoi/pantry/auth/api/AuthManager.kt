package com.ahoi.pantry.auth.api

import io.reactivex.rxjava3.core.Single

interface AuthManager {
    val currentUserId: String

    fun loginWithEmailAndPassword(email: String, password: String): Single<String>

    fun signUpWithEmailAndPassword(email: String, password: String): Single<String>

    fun logout()
}