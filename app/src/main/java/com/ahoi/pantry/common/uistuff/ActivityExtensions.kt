package com.ahoi.pantry.common.uistuff

import android.app.Activity
import android.view.View

fun <T : View> Activity.bind(res: Int) : Lazy<T> {
    return lazy { findViewById(res) }
}