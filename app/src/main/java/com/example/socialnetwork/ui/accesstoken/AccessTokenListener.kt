package com.example.socialnetwork.ui.accesstoken

interface AccessTokenListener {
    fun onCodeReceived(auth_code: String?)
}