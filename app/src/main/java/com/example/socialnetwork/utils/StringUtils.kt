package com.example.socialnetwork.utils

import twitter4j.MediaEntity
import twitter4j.Status

fun String.getClearImageUrl(): String{

    return substring(0, this.indexOf("_normal")) +
            substring(this.indexOf("_normal") + "_normal".length..this.lastIndex)
}