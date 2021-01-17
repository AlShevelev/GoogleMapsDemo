package com.shevelev.googlemapsdemo.shared.pin

import android.util.LruCache

class PinDrawStorage(private val pinDraw: PinDraw) {
    private val cache: LruCache<Int, PinDrawResult>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        val cacheSize = maxMemory / 4

        cache = object : LruCache<Int, PinDrawResult>(cacheSize) {
            override fun sizeOf(key: Int, bitmap: PinDrawResult): Int {
                return bitmap.bitmap.byteCount / 1024
            }
        }
    }

    fun getItem(footprint: PinInfo): PinDrawResult =
        cache.get(footprint.id)
            ?: run {
                pinDraw.draw(
                    footprint.pinBcgColor,
                    footprint.pinTextColor,
                    footprint.image,
                    footprint.title
                )
                    .also {
                        cache.put(footprint.id, it)
                    }
            }
}