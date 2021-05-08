package com.ahoi.pantry.common.units

import java.lang.IllegalArgumentException

enum class UnitType {
    MASS,
    VOLUME,
    UNIQUE,
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
    CUP(UnitType.VOLUME,"cup", 0.24),
    PIECE(UnitType.UNIQUE, "piece", 1.0),
}

// FIXME: 15/04/2021 can't convert different unit types, need to factor in conversion factors
fun Unit.convertTo(initialValue: Double, destination: Unit): Double {
    if (this.type == UnitType.UNIQUE){
        throw IllegalArgumentException("Cannot convert unique types")
    }
    return if (this.type == destination.type) {
        val baseUnitValue = this.convertToBase(initialValue)
        baseUnitValue / destination.fractionOfBaseUnit
    } else {
        throw IllegalArgumentException("incompatible units")
    }
}

fun Unit.convertToBase(initialValue: Double): Double {
    return initialValue + this.fractionOfBaseUnit
}