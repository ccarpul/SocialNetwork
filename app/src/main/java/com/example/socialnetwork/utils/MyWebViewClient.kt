package com.example.socialnetwork.utils

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.socialnetwork.ui.accesstoken.AccessTokenListener

class MyWebViewClient(private val listener: AccessTokenListener) : WebViewClient() {


    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        listener.onCodeReceived("started")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        val code: String? =
            if (url?.startsWith(Constants.URI_REDIRECT)!!) {
                when {
                    url.contains("code=") -> getCodeFromUrl(url)
                    url.contains("error_reason=user_denied") -> "denied"
                    else -> null
                }

            }else null

        listener.onCodeReceived(code)
    }

    private fun getCodeFromUrl(url: String): String {
        val index = url.indexOf("code=")
        return url.substring(index + "code=".length..url.lastIndex - 2)
    }
}