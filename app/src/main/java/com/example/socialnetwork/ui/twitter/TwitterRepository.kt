package com.example.socialnetwork.ui.twitter

import com.example.socialnetwork.Constants
import com.example.socialnetwork.ResultWrapper
import com.example.socialnetwork.data.TwitterApiClient
import com.example.socialnetwork.data.model.ModelTwitter
import com.example.socialnetwork.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TwitterRepository(private val twitterApiClient: TwitterApiClient) {
    private val dispatcher = Dispatchers.IO
    suspend fun getDataTwitter(token: String):
            ResultWrapper<ModelTwitter> =
        withContext(Dispatchers.IO) {
            safeApiCall(dispatcher) {
                twitterApiClient.getProfileTwitter(Constants.AUTHORIZATION)
            }
        }
}