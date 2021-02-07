package com.shevelev.googlemapsdemo.shared.pin

import android.graphics.Bitmap

data class PinDrawResult(
    val bitmap: Bitmap,

    /**
     * Relative X coordinate of a pip spearhead
     */
    val anchorX: Float
)