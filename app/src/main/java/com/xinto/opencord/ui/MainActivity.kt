package com.xinto.opencord.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.xinto.opencord.network.gateway.Gateway
import com.xinto.opencord.network.util.gatewayUrl
import com.xinto.opencord.ui.screens.main.MainScreen
import com.xinto.opencord.ui.theme.OpenCordTheme
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class MainActivity : ComponentActivity() {

    private val gatewayClient: OkHttpClient by inject(named("gateway"))
    private val gateway: Gateway by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gatewayClient.newWebSocket(
            request = Request.Builder().url(gatewayUrl).build(),
            listener = gateway,
        )

        setContent {
            OpenCordTheme {
                MainScreen()
            }
        }
    }
}