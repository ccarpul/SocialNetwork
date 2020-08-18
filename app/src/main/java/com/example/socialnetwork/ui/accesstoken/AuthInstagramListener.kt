package com.example.socialnetwork.ui.accesstoken

interface AuthInstagramListener {
    fun onCodeReceived(auth_code: String)
}