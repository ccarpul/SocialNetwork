package com.example.socialnetwork.utils


fun String.getClearImageUrl(): String{

    return substring(0, this.indexOf("_normal")) +
            substring(this.indexOf("_normal") + "_normal".length..this.lastIndex)
}