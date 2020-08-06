package com.example.socialnetwork.data

import com.example.socialnetwork.Constants
import com.example.socialnetwork.data.model.ModelTwitter
import retrofit2.http.*

interface TwitterApiClient {

    @GET(Constants.ENDPOINT_TWITTER)
    suspend fun getProfileTwitter(@Header("Authorization") oAuth: String) : ModelTwitter
}