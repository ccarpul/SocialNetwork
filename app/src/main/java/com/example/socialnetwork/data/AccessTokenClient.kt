package com.example.socialnetwork.data

import com.example.socialnetwork.data.model.ModelResponseAccessToken
import com.example.socialnetwork.utils.Constants.ENDPOINT_ACCESS_TOKEN
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AccessTokenClient {

    @Multipart
    @POST(ENDPOINT_ACCESS_TOKEN)
    suspend fun getAccessToken(
        @Part("client_id") clientId: RequestBody,
        @Part("client_secret") clientSecret: RequestBody,
        @Part("redirect_uri") redirectUri: RequestBody,
        @Part("grant_type") grantType: RequestBody,
        @Part("code") code: RequestBody
    ): ModelResponseAccessToken
}