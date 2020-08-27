package com.example.socialnetwork.utils

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.socialnetwork.ui.accesstoken.AccessTokenListener

class MyWebViewClient(private val listener: AccessTokenListener) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        Log.i("Carpul", "shouldOverrideUrlLoading: $url")
        if (url?.startsWith(Constants.URI_REDIRECT)!!) {
            if (url.contains("code=")) {
                listener.onCodeReceived(getCodeFromUrl(url))
            }
            if(url.contains("error_reason=user_denied"))
                listener.onCodeReceived("denied")
        }

        return false
    }

    private fun getCodeFromUrl(url: String): String {
        val index = url.indexOf("code=")
        return url.substring(index + "code=".length..url.lastIndex - 2)
    }
}