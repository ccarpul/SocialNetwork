package com.example.socialnetwork.utils

import twitter4j.MediaEntity
import twitter4j.Status

fun mediaEntity(tweet: Status): MediaEntity? {

    return if (tweet.retweetedStatus != null) {
                if (!tweet.retweetedStatus.mediaEntities.isNullOrEmpty())
                    tweet.retweetedStatus.mediaEntities[0]
                else null
            } else if (!tweet.mediaEntities.isNullOrEmpty())
                    tweet.mediaEntities[0]
            else null
}

fun MediaEntity.getMediaUrl(): String{

    return if(this.videoVariants.isNullOrEmpty()) ""
    else this.videoVariants[0].url
}