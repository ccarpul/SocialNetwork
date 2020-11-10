package com.example.socialnetwork.utils

import twitter4j.MediaEntity
import twitter4j.Status

fun Status.mediaEntity(): MediaEntity? =
    retweetedStatus.takeUnless { it != null }?.mediaEntities?.get(0) ?:
    mediaEntities.takeUnless { it.isNullOrEmpty()}?.get(0)


/*
{

    return if (tweet.retweetedStatus != null) {
                if (!tweet.retweetedStatus.mediaEntities.isNullOrEmpty())
                    tweet.retweetedStatus.mediaEntities[0]
                else null
            } else if (!tweet.mediaEntities.isNullOrEmpty())
                    tweet.mediaEntities[0]
            else null
}


 */
fun MediaEntity.getMediaUrl(): String? = this.videoVariants[0].takeIf { it != null }?.url

   // return if(this.videoVariants.isNullOrEmpty()) ""
   // else this.videoVariants[0].url
