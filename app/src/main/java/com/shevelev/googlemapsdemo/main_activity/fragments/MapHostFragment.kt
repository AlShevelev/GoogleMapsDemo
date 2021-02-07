package com.shevelev.googlemapsdemo.main_activity.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.shevelev.googlemapsdemo.R
import com.shevelev.googlemapsdemo.shared.pin.*

/**
 * Fragment with map
 */
class MapHostFragment : Fragment(), OnMapReadyCallback {
    companion object {
        fun newInstance() = MapHostFragment()
    }

    private var map: GoogleMap? = null
    private var clusterManager: ClusterManager<PinInfo>? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map_host, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startMapLoading()
    }

    override fun onMapReady(map: GoogleMap) {
        // turn off rotation and compass
        map.uiSettings.isRotateGesturesEnabled = false
        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isZoomControlsEnabled = false

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.0, -95.0), 2f))

        this.map = map

        showPins()
//        showPinsClustered()
    }

    private fun startMapLoading() {
        val mapFragment = SupportMapFragment.newInstance(GoogleMapOptions())
        childFragmentManager.beginTransaction().add(R.id.mapContainer, mapFragment).commit()
        mapFragment.getMapAsync(this)
    }

    /**
     * Show pins without clusters
     */
    private fun showPins() {
        map?.let { map ->
            val pins = PinInfoRepository.getPins(requireContext().applicationContext)
            val pinsDrawStorage = PinDrawStorage(PinDraw(requireContext().applicationContext))

            pins.forEach {
                val pinDraw = pinsDrawStorage.getItem(it)
                addPin(map, pinDraw, it.position)
            }
        }
    }

    /**
     * Put a pin on a map
     */
    private fun addPin(map: GoogleMap, pin: PinDrawResult, location: LatLng) {
        val markerOptions = MarkerOptions()
            .position(location)
            .icon(BitmapDescriptorFactory.fromBitmap(pin.bitmap))
            .anchor(pin.anchorX, 1f)
        map.addMarker(markerOptions)
    }

    /**
     * Show pins in clusters
     */
    private fun showPinsClustered() {
        map?.let { map ->
            clusterManager = ClusterManager<PinInfo>(requireContext(), map).also { cm ->
                val pinsDrawStorage = PinDrawStorage(PinDraw(requireContext().applicationContext))
                cm.renderer = PinsClusterRenderer(requireContext(), pinsDrawStorage, map, cm)

                // Click on single pin
                cm.setOnClusterItemClickListener {
                    true
                }

                // Process click on a cluster to prevent standard Google map action
                cm.setOnClusterClickListener {
                    true
                }

                // Update clusters on zoom and move
                map.setOnCameraMoveListener {
                    cm.cluster()
                }

                map.setOnMarkerClickListener(cm)

                val pins = PinInfoRepository.getPins(requireContext().applicationContext)

                pins.forEach {
                    cm.addItem(it)
                }

                cm.cluster()
            }
        }
    }
}