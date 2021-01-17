package com.shevelev.googlemapsdemo.main_activity.fragments

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shevelev.googlemapsdemo.R
import com.shevelev.googlemapsdemo.databinding.DialogMapBinding
import com.shevelev.googlemapsdemo.shared.pin.PinDraw
import com.shevelev.googlemapsdemo.shared.pin.PinInfo
import com.shevelev.googlemapsdemo.shared.utils.getScreenSize

/**
 * Dialog with embedded map
 */
class MapDialog : BottomSheetDialogFragment(), OnMapReadyCallback {
    companion object {
        private const val ARG_PIN_BCG_COLOR = "ARG_PIN_BCG_COLOR"
        private const val ARG_PIN_TEXT_COLOR = "ARG_PIN_TEXT_COLOR"
        private const val ARG_IMAGE_URL = "ARG_IMAGE__URL"
        private const val ARG_TEXT = "ARG_TEXT"
        private const val ARG_LOCATION = "ARG_LOCATION"

        fun show(
            activity: AppCompatActivity,
            pinInfo: PinInfo
        ) {
            MapDialog().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PIN_BCG_COLOR, pinInfo.pinBcgColor)
                    putInt(ARG_PIN_TEXT_COLOR, pinInfo.pinTextColor)
                    putParcelable(ARG_IMAGE_URL, pinInfo.image)
                    putString(ARG_TEXT, pinInfo.text)
                    putParcelable(ARG_LOCATION, pinInfo.location)
                }
            }
                .show(activity.supportFragmentManager, null)
        }
    }

    private var binding: DialogMapBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogFragment_RoundCorners)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogMapBinding.inflate(inflater, container, false)

        return binding!!.root.also { view ->
            val screenSize = requireContext().getScreenSize()

            view.findViewById<FrameLayout>(R.id.mapContainer).apply {
                layoutParams = layoutParams.apply {
                    height = (screenSize.height * 0.8).toInt()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                (this as BottomSheetDialog).let { d ->
                    val bottomSheet = d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)!!

                    val behavior = BottomSheetBehavior.from(bottomSheet)

                    // To show the dialog expanded
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED

                    // To prevent an issue in the map scrolling
                    behavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
                        override fun onStateChanged(p0: View, newState: Int) {
                            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                            }
                        }

                        override fun onSlide(p0: View, p1: Float) {
                        }
                    })
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startLoadingMap()

        binding!!.closeButton.setOnClickListener { dismiss() }
    }

    override fun onMapReady(map: GoogleMap) {
        // turn off rotation and compass
        map.uiSettings.isRotateGesturesEnabled = false
        map.uiSettings.isCompassEnabled = false
        map.uiSettings.isZoomControlsEnabled = true

        val args = requireArguments()
        val pinBcgColor = args.getInt(ARG_PIN_BCG_COLOR)
        val pinTextColor = args.getInt(ARG_PIN_TEXT_COLOR)
        val imageUri = requireArguments().getParcelable<Uri>(ARG_IMAGE_URL)!!
        val text = args.getString(ARG_TEXT)
        val location = args.getParcelable<LatLng>(ARG_LOCATION)!!

        // Move a camera and zoom
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))

        val pinDraw = PinDraw(requireContext().applicationContext)

        val pinInfo = pinDraw.draw(pinBcgColor, pinTextColor, imageUri, text)

        // A single marker is added here
        map.addMarker(
            MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(pinInfo.bitmap))
                .anchor(pinInfo.spearheadRelativeX, 1.0f))
    }

    private fun startLoadingMap() {
        val mapFragment = SupportMapFragment.newInstance(GoogleMapOptions())
        childFragmentManager.beginTransaction().add(R.id.mapContainer, mapFragment).commit()
        mapFragment.getMapAsync(this)
    }
}
