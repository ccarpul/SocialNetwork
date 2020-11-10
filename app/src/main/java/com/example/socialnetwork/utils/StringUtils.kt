package com.example.socialnetwork.utils

import java.nio.channels.Selector


fun String.getClearImageUrl(): String{

    return substring(0, indexOf("_normal")) +
            substring(indexOf("_normal") + "_normal".length..lastIndex)
}