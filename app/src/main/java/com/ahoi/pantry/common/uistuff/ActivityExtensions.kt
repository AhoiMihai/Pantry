package com.ahoi.pantry.common.uistuff

import android.app.Activity
import android.view.View
import android.widget.Toast

fun <T : View> Activity.bind(res: Int) : Lazy<T> {
    return lazy { findViewById(res) }
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}