package com.example.socialnetwork.ui.instagram

import com.example.socialnetwork.utils.ResultWrapper
import com.example.socialnetwork.data.InstagramApiClient
import com.example.socialnetwork.data.model.ModelResponse
import com.example.socialnetwork.data.model.ProfileInstagram
import com.example.socialnetwork.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InstagramRespository(private val instagramApiClient: InstagramApiClient) {
    val dispatcher = Dispatchers.IO

    suspend fun getDataInstagram(after: String):
            ResultWrapper<ModelResponse> =
            withContext(Dispatchers.IO) {
                safeApiCall(dispatcher) {
                    instagramApiClient.getMedia(after)
                }
            }
    suspend fun getProfileInstagram():
            ResultWrapper<ProfileInstagram> =
        withContext(Dispatchers.IO) {
            safeApiCall(dispatcher) {
                instagramApiClient.getProfile()
            }
        }
}