package com.ahoi.pantry.common.units

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quantity(
    val amount: Double,
    val unit: Unit
) : Parcelable