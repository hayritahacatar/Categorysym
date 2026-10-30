package com.example.categorysym.ui.components.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.util.Locale
import kotlin.math.abs

class NestedScrollWebView(context: Context) : WebView(context) {
    private var startX = 0f
    private var startY = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                parent?.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = abs(event.x - startX)
                val dy = abs(event.y - startY)

                if (dy > dx) {
                    // Vertical scroll has priority, disallow parent HorizontalPager from intercepting
                    parent?.requestDisallowInterceptTouchEvent(true)
                } else if (dx > dy && dx > 15f) {
                    // Horizontal swipe gesture, allow HorizontalPager to handle page swipe
                    parent?.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent?.requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.onTouchEvent(event)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DiscoverScreen(onWebViewCreated: (WebView) -> Unit) {
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(value = false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Simple Top Bar for WebView Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
        ) {
            IconButton(
                onClick = { webViewInstance?.goBack() },
                enabled = canGoBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        AndroidView(
            modifier = Modifier.weight(1f),
            factory = { context ->
                NestedScrollWebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    
                    setLayerType(View.LAYER_TYPE_HARDWARE, null)
                    isNestedScrollingEnabled = true
                    overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            canGoBack = view?.canGoBack() ?: false
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            canGoBack = view?.canGoBack() ?: false
                        }
                    }

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        cacheMode = WebSettings.LOAD_DEFAULT
                        @Suppress("DEPRECATION")
                        setRenderPriority(WebSettings.RenderPriority.HIGH)
                        userAgentString =
                            "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36"
                    }

                    val isTurkish = Locale.getDefault().language == "tr"
                    val newsUrl = if (isTurkish) {
                        "https://news.google.com/topstories?hl=tr&gl=TR&ceid=TR:tr"
                    } else {
                        "https://news.google.com"
                    }
                    loadUrl(newsUrl)
                    
                    webViewInstance = this
                    onWebViewCreated(this)
                }
            },
            update = {
                // Do not mutate Compose state inside update lambda
            }
        )
    }
}