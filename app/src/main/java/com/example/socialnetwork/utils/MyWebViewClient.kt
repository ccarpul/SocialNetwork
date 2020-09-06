package com.example.socialnetwork.utils

import android.graphics.Bitmap
import android.util.Log
import android.webkit.*
import com.example.socialnetwork.ui.accesstoken.AccessTokenListener

class MyWebViewClient(private val listener: AccessTokenListener) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        view?.loadUrl(request?.url.toString())
        return true
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

        view?.loadUrl(url)
        return true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        val code: String? =
            if (url?.startsWith(Constants.URI_REDIRECT)!!) {
                when {
                    //url.contains("code=") -> getCodeFromUrl(url)
                    "code=" in url -> getCodeFromUrl(url)
                    //url.contains("error_reason=user_denied") -> "denied"
                    "error_reason=user_denied" in url -> "denied"
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