package com.example.splashscreendemo

import android.content.res.Resources
import android.util.Log

fun log(text: String) {
    Log.d("Splash screen demo", text)
}

fun dp2px(dips: Float): Float = dips * Resources.getSystem().displayMetrics.density + 0.5f
