package com.camilasouza.sampleapp.components


import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.core.files.delegates.GeolocationPermissionDelegate
import dev.hotwire.navigation.destinations.HotwireDestination
import dev.hotwire.navigation.fragments.HotwireFragment
import kotlinx.serialization.Serializable

// Requires additional configuration. See the documentation for details.
// https://github.com/joemasilotti/bridge-components/tree/main/components/location
class LocationComponent(
    name: String,
    private val bridgeDelegate: BridgeDelegate<HotwireDestination>
) : BridgeComponent<HotwireDestination>(name, bridgeDelegate) {

    private val fragment: HotwireFragment
        get() = bridgeDelegate.destination.fragment as HotwireFragment
    private val geolocationPermissionDelegate =
        GeolocationPermissionDelegate(fragment.navigator.session)
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(fragment.requireContext())
    }

    override fun onReceive(message: Message) {
        when (message.event) {
            "get" -> getLocation(message)
            else -> Log.w("Location Component", "Unknown event for message: $message")
        }
    }

    private fun getLocation(message: Message) {
        geolocationPermissionDelegate.onRequestPermission(
            origin = fragment.navigator.location,
            callback = { _, allow, _ ->
                if (allow) {
                    getUserLocation(message)
                } else {
                    replyTo(message.event, LocationMessageData(null, null))
                }
            }
        )
    }

    @SuppressLint("MissingPermission") // Permission is checked in GeolocationPermissionDelegate.
    private fun getUserLocation(message: Message) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                replyTo(message.event, LocationMessageData(location.latitude, location.longitude))
            } else {
                requestFreshLocation(message)
            }
        }.addOnFailureListener { e ->
            requestFreshLocation(message)
        }
    }

    @SuppressLint("MissingPermission") // Permission is checked in GeolocationPermissionDelegate.
    private fun requestFreshLocation(message: Message) {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location ->
            if (location != null) {
                replyTo(message.event, LocationMessageData(location.latitude, location.longitude))
            } else {
                replyTo(message.event, LocationMessageData(null, null))
            }
        }.addOnFailureListener { e ->
            replyTo(message.event, LocationMessageData(null, null))
        }
    }

    @Serializable
    data class LocationMessageData(
        val latitude: Double?,
        val longitude: Double?
    )
}