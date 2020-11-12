package com.example.socialnetwork.ui.instagram

import com.example.socialnetwork.utils.ResultWrapper
import com.example.socialnetwork.data.InstagramApiClient
import com.example.socialnetwork.data.model.ModelResponseInstagram
import com.example.socialnetwork.data.model.ModelResponseProfileInstagram
import com.example.socialnetwork.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InstagramRespository(private val instagramApiClient: InstagramApiClient) {
    val dispatcher = Dispatchers.IO

    suspend fun getDataInstagram(after: String, code: String?):
            ResultWrapper<ModelResponseInstagram> =
            withContext(Dispatchers.IO) {
                safeApiCall(dispatcher) {
                    instagramApiClient.getMedia(after, code)
                }
            }
    suspend fun getProfileInstagram(code: String?):
            ResultWrapper<ModelResponseProfileInstagram> =
        withContext(Dispatchers.IO) {
            safeApiCall(dispatcher) {
                instagramApiClient.getProfile(code)
            }
        }
}