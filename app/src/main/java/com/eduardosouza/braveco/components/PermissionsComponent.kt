package com.camilasouza.sampleapp.components


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.destinations.HotwireDestination
import dev.hotwire.navigation.fragments.HotwireFragment
import kotlinx.serialization.Serializable

// Requires additional configuration. See the documentation for details.
// https://github.com/joemasilotti/bridge-components/tree/main/components/permissions
class PermissionsComponent(
    name: String,
    private val bridgeDelegate: BridgeDelegate<HotwireDestination>
) : BridgeComponent<HotwireDestination>(name, bridgeDelegate) {
    private val fragment: HotwireFragment
        get() = bridgeDelegate.destination.fragment as HotwireFragment

    override fun onReceive(message: Message) {
        when (message.event) {
            "camera" -> getCameraPermission(message.event)
            "location" -> getLocationPermission(message.event)
            "notifications" -> getNotificationsPermission(message.event)
            else -> Log.w("Permissions", "Unknown event for message: $message")
        }
    }

    private fun getCameraPermission(event: String) {
        val context = fragment.requireContext()

        if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            replyTo(event, StatusMessageData(1, "authorized"))
        } else {
            replyTo(event, StatusMessageData(0, "denied"))
        }
    }

    private fun getLocationPermission(event: String) {
        val context = fragment.requireContext()
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (permissions.any { context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }) {
            replyTo(event, StatusMessageData(1, "authorized"))
        } else {
            replyTo(event, StatusMessageData(0, "denied"))
        }
    }

    private fun getNotificationsPermission(event: String) {
        val context = fragment.requireContext()
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                PackageManager.PERMISSION_GRANTED -> StatusMessageData(1, "authorized")
                // There is no "not determined" state for this permission on Android.
                // Before asking the user for permission, it will return PERMISSION_DENIED.
                PackageManager.PERMISSION_DENIED -> StatusMessageData(0, "denied")
                else -> StatusMessageData(-2, "unknown")
            }
        } else {
            StatusMessageData(1, "authorized")
        }

        replyTo(event, data)
    }

    @Serializable
    data class StatusMessageData(
        val code: Int,
        val text: String
    )
}