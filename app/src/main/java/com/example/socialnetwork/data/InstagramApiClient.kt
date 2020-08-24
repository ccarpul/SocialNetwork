package com.example.socialnetwork.data

import com.example.socialnetwork.utils.Constants
import com.example.socialnetwork.data.model.ModelResponse
import com.example.socialnetwork.data.model.ModelResponseProfileInstagram
import retrofit2.http.*

interface InstagramApiClient {

    @GET(Constants.ENDPOINT_MEDIA)
    suspend fun getMedia(
        @Query("after") after: String,
        @Query("access_token") code: String
    ): ModelResponse

    @GET(Constants.ENDPOINT_ME)
    suspend fun getProfile(@Query("access_token") code: String): ModelResponseProfileInstagram


}