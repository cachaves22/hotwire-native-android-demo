package com.camilasouza.sampleapp.components

import android.util.Log
import android.widget.Toast
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeDelegate
import dev.hotwire.core.bridge.Message
import dev.hotwire.navigation.destinations.HotwireDestination
import dev.hotwire.navigation.fragments.HotwireFragment
import kotlinx.serialization.Serializable

// Requires additional configuration. See the documentation for details.
// https://github.com/joemasilotti/bridge-components/tree/main/components/barcode-scanner
class BarcodeScannerComponent(
    name: String,
    private val bridgeDelegate: BridgeDelegate<HotwireDestination>
) : BridgeComponent<HotwireDestination>(name, bridgeDelegate) {
    private val fragment: HotwireFragment
        get() = bridgeDelegate.destination.fragment as HotwireFragment

    override fun onReceive(message: Message) {
        when (message.event) {
            "scan" -> scan(message.event)
            else -> Log.w("Barcode Scanner Component", "Unknown event for message: $message")
        }
    }

    private fun scan(event: String) {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_EAN_8,
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_PDF417,
                Barcode.FORMAT_CODE_128
            )
            .build()
        val scanner = GmsBarcodeScanning.getClient(fragment.requireContext(), options)
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                barcode.rawValue?.let {
                    replyTo(event, ScanMessageData(it))
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(fragment.requireContext(), "Failed to scan: $e", Toast.LENGTH_LONG)
                    .show()
            }
    }

    @Serializable
    data class ScanMessageData(
        val barcode: String
    )
}