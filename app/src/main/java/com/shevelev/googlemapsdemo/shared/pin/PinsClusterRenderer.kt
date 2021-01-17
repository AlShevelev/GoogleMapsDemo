package com.shevelev.googlemapsdemo.shared.pin

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class PinsClusterRenderer(
    context: Context,
    private val pinDrawStorage: PinDrawStorage,
    map: GoogleMap,
    clusterManager: ClusterManager<PinInfo>
) : DefaultClusterRenderer<PinInfo>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: PinInfo, markerOptions: MarkerOptions) {
        val pin = pinDrawStorage.getItem(item)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(pin.bitmap))
        markerOptions.anchor(pin.spearheadRelativeX, 1f)
    }

    override fun onClusterItemUpdated(item: PinInfo, marker: Marker) {
        val pin = pinDrawStorage.getItem(item)
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(pin.bitmap))
        marker.setAnchor(pin.spearheadRelativeX, 1f)
    }
}