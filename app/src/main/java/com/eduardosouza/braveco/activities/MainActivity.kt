package com.camilasouza.sampleapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.camilasouza.sampleapp.BuildConfig
import com.camilasouza.sampleapp.R
import com.camilasouza.sampleapp.models.Tab
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.turbo.config.PathConfiguration
import dev.hotwire.navigation.activities.HotwireActivity
import dev.hotwire.navigation.navigator.NavigatorConfiguration
import dev.hotwire.navigation.util.applyDefaultImeWindowInsets


const val baseURL = BuildConfig.BASE_URL

class MainActivity : HotwireActivity() {
    private val tabs = Tab.all

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.sampleapp_nav_host).applyDefaultImeWindowInsets()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { tab ->
            val selectedTab = tabs.first { it.menuId == tab.itemId }
            showTab(selectedTab)
            true
        }
        showTab(tabs.first())

        Hotwire.loadPathConfiguration(
            context = this,
            location = PathConfiguration.Location(
                remoteFileUrl = "$baseURL/configurations/android_v1.json"
            )
        )
    }

    override fun onStart() {
        super.onStart()

        handleDeepLink(intent)
    }

    override fun navigatorConfigurations() = tabs.map { tab ->
        NavigatorConfiguration(
            name = tab.name,
            startLocation = "$baseURL/${tab.path}",
            navigatorHostId = tab.navigatorHostId
        )
    }

    private fun showTab(tab: Tab) {
        tabs.forEach {
            val view = findViewById<View>(it.navigatorHostId)
            view.visibility = if (it == tab) View.VISIBLE else View.GONE
        }
    }

    private fun handleDeepLink(intent: Intent?) {
        val path = intent?.getStringExtra("path")
        path?.let {
            delegate.currentNavigator?.route("$baseURL$it")
        }
        this.intent = null
    }
}
