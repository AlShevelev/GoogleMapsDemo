package com.shevelev.googlemapsdemo.main_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.shevelev.googlemapsdemo.R
import com.shevelev.googlemapsdemo.main_activity.fragments.MapDialog
import com.shevelev.googlemapsdemo.main_activity.fragments.MapHostFragment
import com.shevelev.googlemapsdemo.main_activity.fragments.MapStubFragment
import com.shevelev.googlemapsdemo.main_activity.fragments.MenuFragment
import com.shevelev.googlemapsdemo.shared.pin.PinInfoRepository

/**
 * A main (and single) activity of the application
 */
class MainActivity : AppCompatActivity(), MainActivityActions {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            updateFragment {
                add(R.id.fragmentContainer, MenuFragment.newInstance())
            }
        }
    }

    private fun updateFragment(fragmentAction: FragmentTransaction.() -> Unit) {
        supportFragmentManager.beginTransaction().apply {
            fragmentAction(this)
            commit()
        }
    }

    override fun onMapInFragmentClick() {
        if(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            updateFragment {
                replace(R.id.fragmentContainer, MapHostFragment.newInstance())
                addToBackStack(null)

            }
        } else {
            updateFragment {
                replace(R.id.fragmentContainer, MapStubFragment.newInstance())
                addToBackStack(null)
            }
        }
    }

    override fun onMapInDialogClick() {
        MapDialog.show(this, PinInfoRepository.getPins(this)[8])
    }
}