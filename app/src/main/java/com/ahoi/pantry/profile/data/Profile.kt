package com.ahoi.pantry.profile.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val name: String,
    val email: String,
    val pantryReference: String
) : Parcelable