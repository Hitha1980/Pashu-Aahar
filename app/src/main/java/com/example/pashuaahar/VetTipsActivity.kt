package com.example.pashuaahar

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient

class VetTipsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this)
        setContentView(webView)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        
        val searchQuery = getString(R.string.tips_search_query)
        webView.loadUrl("https://www.youtube.com/results?search_query=$searchQuery")
    }
}
