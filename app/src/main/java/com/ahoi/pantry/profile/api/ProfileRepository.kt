package com.ahoi.pantry.profile.api

import com.ahoi.pantry.profile.data.Profile
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ProfileRepository {

    fun createProfile(id: String, name: String, email: String): Completable

    fun updateProfilePantryRef(id: String, reference: String): Completable

    fun updateProfile(profile: Profile): Completable

    fun loadProfile(id: String): Completable

    fun getOrLoadCurrent(): Single<Profile>

    fun getOrLoadPantryReference(): Single<String>

    fun clearProfile()

}