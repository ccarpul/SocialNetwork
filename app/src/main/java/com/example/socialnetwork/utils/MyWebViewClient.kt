package com.example.socialnetwork.utils

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.socialnetwork.ui.accesstoken.AuthInstagramListener

class MyWebViewClient(private val listener: AuthInstagramListener) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        Log.i("Carpul", "shouldOverrideUrlLoading: $url")
        if (url?.startsWith(Constants.URI_REDIRECT)!!)
            if (url.contains("code=")) {
                listener.onCodeReceived(getCodeFromUrl(url))
            }
        return false
    }

    private fun getCodeFromUrl(url: String): String {
        val index = url.indexOf("code=")
        return url.substring(index + "code=".length..url.lastIndex - 2)
    }
}