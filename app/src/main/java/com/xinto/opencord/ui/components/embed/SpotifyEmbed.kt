package com.xinto.opencord.ui.components.embed

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@Composable
fun SpotifyEmbed(
    embedUrl: String,
    isSpotifyTrack: Boolean,
) {
    WebView(
        state = rememberWebViewState(embedUrl),
        captureBackPresses = false,
        onCreated = { webView ->
            webView.setBackgroundColor(Color.TRANSPARENT)
            webView.settings.apply {
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true
                allowFileAccess = false
                mediaPlaybackRequiresUserGesture = false
                cacheMode = WebSettings.LOAD_NO_CACHE
                setGeolocationEnabled(false)
            }
        },
        factory = { ScrollableWebView(it) },
        modifier = Modifier
            .height(if (isSpotifyTrack) 80.dp else 500.dp),
    )
}

private class ScrollableWebView(context: Context) : WebView(context) {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        requestDisallowInterceptTouchEvent(true)
        return super.onTouchEvent(event)
    }
}
