package com.shevelev.googlemapsdemo.shared.utils

import android.content.Context
import android.content.res.Resources
import android.util.Size

/**
 * Get screen size in pixels
 */
fun Context.getScreenSize(): Size =
    Resources.getSystem().displayMetrics.let {
        Size(it.widthPixels, it.heightPixels)
    }
