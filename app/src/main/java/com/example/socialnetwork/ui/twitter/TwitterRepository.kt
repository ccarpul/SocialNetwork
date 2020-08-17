package com.example.socialnetwork.ui.twitter

import android.util.Log
import com.example.socialnetwork.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder
import java.util.stream.Collectors

class TwitterRepository {
    private val dispatcher = Dispatchers.IO
    suspend fun getTimeLineTwitter(userToken: String?, userTokenSecret: String?) {//:

        withContext(dispatcher) {

            val config = ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(Constants.CONSUMER_KEY)
                .setOAuthConsumerSecret(Constants.CONSUMER_SECRET)
                .setOAuthAccessToken(userToken)
                .setOAuthAccessTokenSecret(userTokenSecret)
            val twitter = TwitterFactory(config.build()).instance


            val timeLine = twitter.homeTimeline.stream()
                .map { item -> item.text }
                .collect(Collectors.toList())
            Log.i("Carpul", "getDataTwitter: $timeLine")
        }
    }
}