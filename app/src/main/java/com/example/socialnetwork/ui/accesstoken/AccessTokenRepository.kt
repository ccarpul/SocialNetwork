package com.example.socialnetwork.ui.accesstoken

import android.util.Log
import com.example.socialnetwork.data.InstagramAccessTokenClient
import com.example.socialnetwork.utils.ResultWrapper
import com.example.socialnetwork.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody

class AccessTokenRepository(private val instagramAccessTokenClient: InstagramAccessTokenClient) {

    val dispatcher = Dispatchers.IO

    suspend fun getAccessToken(clientId: RequestBody, clientSecret: RequestBody,
                               redirectUri: RequestBody, grantType: RequestBody, code: RequestBody
    ):
            ResultWrapper<ModelAccessToken> =
        withContext(Dispatchers.IO) {
            safeApiCall(dispatcher) {
                instagramAccessTokenClient.getAccessToken(clientId, clientSecret,
                    redirectUri,
                    grantType,
                    code)
            }
        }
}