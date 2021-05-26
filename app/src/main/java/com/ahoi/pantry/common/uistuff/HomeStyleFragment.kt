package com.ahoi.pantry.common.uistuff

abstract class HomeStyleFragment : PantryFragment() {

    abstract val titleResId: Int

    abstract fun activityFabClicked()
}