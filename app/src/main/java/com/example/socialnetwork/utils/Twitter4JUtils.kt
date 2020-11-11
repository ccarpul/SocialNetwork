package com.example.socialnetwork.utils

import twitter4j.MediaEntity
import twitter4j.Status

fun Status.mediaEntity(): MediaEntity? =
    retweetedStatus.takeUnless {
        it != null }?.mediaEntities?.get(0) ?:
    mediaEntities.takeUnless { it.isNullOrEmpty()}?.get(0)

fun MediaEntity.getMediaUrl(): String = videoVariants[0].takeIf { it != null }?.url ?: ""

