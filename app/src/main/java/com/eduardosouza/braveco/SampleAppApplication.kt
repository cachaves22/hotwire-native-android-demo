package com.camilasouza.sampleapp

import android.app.Application

import com.camilasouza.sampleapp.components.NotificationTokenComponent

import com.camilasouza.sampleapp.fragments.WebFragment
import com.google.firebase.FirebaseApp
import dev.hotwire.core.bridge.BridgeComponentFactory
import dev.hotwire.core.bridge.KotlinXJsonConverter
import dev.hotwire.core.config.Hotwire
import dev.hotwire.navigation.config.defaultFragmentDestination
import dev.hotwire.navigation.config.registerBridgeComponents
import dev.hotwire.navigation.config.registerFragmentDestinations
import com.camilasouza.sampleapp.components.AlertComponent
import com.camilasouza.sampleapp.components.BarcodeScannerComponent
import com.camilasouza.sampleapp.components.ButtonComponent
import com.camilasouza.sampleapp.components.FormComponent
import com.camilasouza.sampleapp.components.LocationComponent
import com.camilasouza.sampleapp.components.MenuComponent
import com.camilasouza.sampleapp.components.PermissionsComponent
import com.camilasouza.sampleapp.components.ReviewPromptComponent
import com.camilasouza.sampleapp.components.ShareComponent


class sampleappApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        Hotwire.registerFragmentDestinations(WebFragment::class)
        Hotwire.defaultFragmentDestination = WebFragment::class

        Hotwire.registerBridgeComponents(
            BridgeComponentFactory("button", ::ButtonComponent),
            BridgeComponentFactory("form", ::FormComponent),
            BridgeComponentFactory("barcode", ::BarcodeScannerComponent),
            BridgeComponentFactory("location", ::LocationComponent),
            BridgeComponentFactory("permissions", ::PermissionsComponent),
            BridgeComponentFactory("share", ::ShareComponent),
            BridgeComponentFactory("notification-token", ::NotificationTokenComponent),
            BridgeComponentFactory("review-prompt", ::ReviewPromptComponent),
            BridgeComponentFactory("alert", ::AlertComponent),
            BridgeComponentFactory("menu", ::MenuComponent),
        )

        Hotwire.config.jsonConverter = KotlinXJsonConverter()
        Hotwire.config.applicationUserAgentPrefix = "sampleapp Android Hotwire"

    }
}