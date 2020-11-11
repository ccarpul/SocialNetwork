package com.example.socialnetwork.utils

fun String.getClearImageUrl(): String = substring(0, indexOf("_normal")) +
            substring(indexOf("_normal") + "_normal".length..lastIndex)
