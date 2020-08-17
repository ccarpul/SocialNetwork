package com.example.socialnetwork.data

import com.example.socialnetwork.utils.Constants
import com.example.socialnetwork.data.model.ModelResponse
import com.example.socialnetwork.data.model.ProfileInstagram
import retrofit2.http.GET
import retrofit2.http.Query

interface InstagramApiClient {

    @GET("${Constants.ENDPOINT_MEDIA}${Constants.APIKEY}")
    suspend fun getMedia(@Query("after") after: String): ModelResponse

    @GET("${Constants.ENDPOINT_ME}${Constants.APIKEY}")
    suspend fun getProfile(): ProfileInstagram

}