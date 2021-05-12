package com.ahoi.pantry.common.units

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quantity(
    val amount: Double,
    val unit: Unit
) : Parcelable, Comparable<Quantity> {
    override fun compareTo(other: Quantity): Int {
        return when {
            amount > other.convertTo(this.unit) -> 1
            amount > other.convertTo(this.unit) -> -1
            else -> 0
        }
    }
}