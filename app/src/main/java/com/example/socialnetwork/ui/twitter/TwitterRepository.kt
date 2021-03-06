package com.example.socialnetwork.ui.twitter

import com.example.socialnetwork.utils.Constants
import com.example.socialnetwork.utils.ResultWrapper
import com.example.socialnetwork.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import twitter4j.*
import twitter4j.conf.ConfigurationBuilder

class TwitterRepository() {
    private val dispatcher = Dispatchers.IO
    suspend fun getTimeLineTwitter(userToken: String?, userTokenSecret: String?, page: Int):
            ResultWrapper<List<Status>> =

        withContext(Dispatchers.IO) {
            safeApiCall(dispatcher) {

                val paging = Paging(page, 30)
                val config = ConfigurationBuilder()
                    .setDebugEnabled(true)
                    .setOAuthConsumerKey(Constants.CONSUMER_KEY)
                    .setOAuthConsumerSecret(Constants.CONSUMER_SECRET)
                    .setOAuthAccessToken(userToken)
                    .setOAuthAccessTokenSecret(userTokenSecret)
                val twitter = TwitterFactory(config.build()).instance
                twitter.getHomeTimeline(paging) as List<Status>
            }
        }
}