package com.shevelev.googlemapsdemo.main_activity.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shevelev.googlemapsdemo.R

/**
 * This fragment is shown if Google Play services are unavailable
 */
class MapStubFragment : Fragment() {
    companion object {
        fun newInstance() = MapStubFragment()
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map_stub, null)
    }
}