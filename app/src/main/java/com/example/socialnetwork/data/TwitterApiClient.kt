package com.example.socialnetwork.data

import com.example.socialnetwork.data.model.ModelTwitter
import com.example.socialnetwork.data.model.ProfileInstagram
import retrofit2.http.*

interface TwitterApiClient {

    @GET("1.1/statuses/home_timeline.json")
    suspend fun getProfileTwitter() : ModelTwitter
}