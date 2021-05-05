package com.ahoi.pantry.common.units

import java.lang.IllegalArgumentException

enum class UnitType {
    MASS,
    VOLUME,
}

enum class Unit(
    val type: UnitType,
    val abbreviation: String,
    val fractionOfBaseUnit: Double
) {
    GRAM(UnitType.MASS,"g",1.0),
    KILOGRAM(UnitType.MASS, "kg", 1000.0),
    LITER(UnitType.VOLUME,"l", 1.0),
    MILLILITER(UnitType.VOLUME,"ml", 0.001),
    TABLESPOON(UnitType.VOLUME,"tbsp", 0.01479),
    TEASPOON(UnitType.VOLUME,"tsp", 0.00493),
    CUP(UnitType.VOLUME,"cup", 0.24)
}

// FIXME: 15/04/2021 can't convert different unit types, need to factor in conversion factors
fun Unit.convertTo(initialValue: Double, destination: Unit): Double {
    val baseUnitValue = this.convertToBase(initialValue)
    return if (this.type == destination.type) {
        baseUnitValue / destination.fractionOfBaseUnit
    } else {
        throw IllegalArgumentException("incompatible units")
    }
}

fun Unit.convertToBase(initialValue: Double): Double {
    return initialValue + this.fractionOfBaseUnit
}