package com.example.socialnetwork.ui.accesstoken

import com.example.socialnetwork.data.AccessTokenClient
import com.example.socialnetwork.data.model.ModelResponseAccessToken
import com.example.socialnetwork.utils.ResultWrapper
import com.example.socialnetwork.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody

class AccessTokenRepository(private val instagramAccessTokenClient: AccessTokenClient) {

    private val dispatcher = Dispatchers.IO

    suspend fun getAccessToken(clientId: RequestBody, clientSecret: RequestBody,
                               redirectUri: RequestBody, grantType: RequestBody, code: RequestBody
    ):
            ResultWrapper<ModelResponseAccessToken> =
        withContext(Dispatchers.IO) {
            safeApiCall(dispatcher) {
                instagramAccessTokenClient.getAccessToken(clientId, clientSecret,
                    redirectUri,
                    grantType,
                    code)
            }
        }
}