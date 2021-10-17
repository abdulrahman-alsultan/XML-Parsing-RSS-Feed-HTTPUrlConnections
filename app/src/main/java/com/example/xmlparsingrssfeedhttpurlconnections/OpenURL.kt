package com.example.xmlparsingrssfeedhttpurlconnections

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi

class OpenURL : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_u_r_l)

        val web = findViewById<WebView>(R.id.web_view)
        web.settings.javaScriptEnabled = true

        val url = intent.getStringExtra("url").toString()!!.replace("http", "https")
        this.title = intent.getStringExtra("title")
        web.webViewClient = MyWebViewClient(this)
        web.loadUrl(url)

    }

    class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString();
            view?.loadUrl(url)
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            webView.loadUrl(url)
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            Toast.makeText(activity, "Got Error! $error", Toast.LENGTH_SHORT).show()
        }
    }
}