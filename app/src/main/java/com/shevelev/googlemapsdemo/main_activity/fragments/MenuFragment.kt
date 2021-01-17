package com.shevelev.googlemapsdemo.main_activity.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shevelev.googlemapsdemo.databinding.FragmentMenuBinding
import com.shevelev.googlemapsdemo.main_activity.MainActivityActions

/**
 * Fragment with use cases
 */
class MenuFragment : Fragment() {
    companion object {
        fun newInstance() = MenuFragment()
    }

    private var binding: FragmentMenuBinding? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.mapInFragmentButton?.setOnClickListener {
            (activity as? MainActivityActions)?.onMapInFragmentClick()
        }

        binding?.mapInDialogButton?.setOnClickListener {
            (activity as? MainActivityActions)?.onMapInDialogClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
