package com.camilasouza.sampleapp.viewmodels

import android.webkit.CookieManager
import com.camilasouza.sampleapp.activities.baseURL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class NotificationTokenViewModel {
    suspend fun registerToken(token: String) = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseURL/notification_tokens")

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty(
                    "Content-Type",
                    "application/json"
            )

            CookieManager.getInstance().getCookie(baseURL)?.let {
                connection.setRequestProperty("Cookie", it)
            }

            val body = JSONObject().apply {
                put("token", token)
                put("platform", "FCM")
            }

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(body.toString())
            }
            connection.responseCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}