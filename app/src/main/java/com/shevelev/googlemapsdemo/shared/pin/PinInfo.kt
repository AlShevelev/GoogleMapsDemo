package com.shevelev.googlemapsdemo.shared.pin

import android.net.Uri
import androidx.annotation.ColorInt
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class PinInfo (
    val id: Int,
    val location: LatLng,
    val image: Uri,
    val text: String,
    @ColorInt
    val pinTextColor: Int,
    @ColorInt
    val pinBcgColor: Int
) : ClusterItem {
    /**
     * The position of this marker. This must always return the same value.
     */
    override fun getPosition(): LatLng = location

    /**
     * The title of this marker.
     */
    override fun getTitle(): String = text

    /**
     * The description of this marker.
     */
    override fun getSnippet(): String? = null
}