package com.shevelev.googlemapsdemo.shared.pin

import android.content.Context
import android.graphics.Color
import android.net.Uri
import com.google.android.gms.maps.model.LatLng

object PinInfoRepository {
    fun getPins(context: Context): List<PinInfo> {
        return listOf(
            // Los Angeles
            PinInfo(
                id = 5,
                LatLng(34.069021, -118.338606),
                getImageUri("la_1", context),
                "Somewhere in Los Angeles",
                pinTextColor = Color.BLACK,
                pinBcgColor = Color.WHITE),

            PinInfo(
                id = 0,
                LatLng(34.130882, -118.333935),
                getImageUri("la_2", context),
                "Somewhere in Los Angeles",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.RED),

            PinInfo(
                id = 1,
                LatLng(34.130882, -118.333935),
                getImageUri("la_3", context),
                "Somewhere in Los Angeles",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.GREEN),

            // Las Vegas
            PinInfo(
                id = 2,
                LatLng(36.218711, -115.168924),
                getImageUri("lv_1", context),
                "Somewhere in Las Vegas",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.BLUE),

            PinInfo(
                id = 3,
                LatLng(36.158705, -115.140696),
                getImageUri("lv_2", context),
                "Somewhere in Las Vegas",
                pinTextColor = Color.BLACK,
                pinBcgColor = Color.YELLOW),

            PinInfo(
                id = 4,
                LatLng(36.107678, -115.216480),
                getImageUri("lv_3", context),
                "Somewhere in Las Vegas",
                pinTextColor = Color.BLACK,
                pinBcgColor = Color.CYAN),

            // NYC
            PinInfo(
                id = 6,
                LatLng(40.766724, -73.982773),
                getImageUri("ny_1", context),
                "Somewhere in NYC",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.BLACK),

            PinInfo(
                id = 7,
                LatLng(40.801039, -73.950601),
                getImageUri("ny_2", context),
                "Somewhere in NYC",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.MAGENTA),

            PinInfo(
                id = 8,
                LatLng(40.716980, -74.004165),
                getImageUri("ny_3", context),
                "Somewhere in NYC",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.RED),

            // Philadelphia
            PinInfo(
                id = 9,
                LatLng(39.978796, -75.158089),
                getImageUri("phd_1", context),
                "Somewhere in Philadelphia",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.GREEN),

            PinInfo(
                id = 10,
                LatLng(39.945172, -75.172705),
                getImageUri("phd_2", context),
                "Somewhere in Philadelphia",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.GRAY),

            PinInfo(
                id = 11,
                LatLng(39.964878, -75.224626),
                getImageUri("phd_3", context),
                "Somewhere in Philadelphia",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.BLACK),

            // San Francisco
            PinInfo(
                id = 12,
                LatLng(37.771954, -122.461892),
                getImageUri("sf_1", context),
                "Somewhere in San Francisco",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.BLUE),

            PinInfo(
                id = 13,
                LatLng(37.746929, -122.442937),
                getImageUri("sf_2", context),
                "Somewhere in San Francisco",
                pinTextColor = Color.BLACK,
                pinBcgColor = Color.YELLOW),

            PinInfo(
                id = 14,
                LatLng(37.727466, -122.172278),
                getImageUri("sf_3", context),
                "Somewhere in San Francisco",
                pinTextColor = Color.BLACK,
                pinBcgColor = Color.CYAN),

            // Washington DC
            PinInfo(
                id = 15,
                LatLng(38.929846, -77.069656),
                getImageUri("wdc_1", context),
                "Somewhere in Washington DC",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.RED),

            PinInfo(
                id = 16,
                LatLng(38.889568, -77.036794),
                getImageUri("wdc_2", context),
                "Somewhere in Washington DC",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.GREEN),

            PinInfo(
                id = 17,
                LatLng(38.881952, -77.101226),
                getImageUri("wdc_3", context),
                "Somewhere in Washington DC",
                pinTextColor = Color.WHITE,
                pinBcgColor = Color.BLUE)
            )
    }

    private fun getImageUri(imageName: String, context: Context) =
        Uri.parse("android.resource://${context.packageName}/raw/$imageName")
}