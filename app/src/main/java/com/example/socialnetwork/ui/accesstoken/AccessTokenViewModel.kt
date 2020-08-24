package com.example.socialnetwork.ui.accesstoken

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialnetwork.data.model.ModelResponseAccessToken
import com.example.socialnetwork.utils.Constants
import com.example.socialnetwork.utils.ResultWrapper
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.RequestBody
import kotlin.coroutines.CoroutineContext

class AccessTokenViewModel(private val accessTokenRepository: AccessTokenRepository) : ViewModel(),
    CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val _modelAccessToken= MutableLiveData<StateLiveData>()
    val modelAccessToken: LiveData<StateLiveData> = _modelAccessToken


    private val clientId: RequestBody =
        RequestBody.create(MediaType.parse("text/plain"), Constants.CONSUMER_ID_INSTAGRAM)
    private val clientSecret: RequestBody =
        RequestBody.create(MediaType.parse("text/plain"), Constants.CONSUMER_SECRET_INSTAGRAM)
    private val redirectUri: RequestBody =
        RequestBody.create(MediaType.parse("text/plain"), Constants.URI_REDIRECT)
    private val grantType: RequestBody =
        RequestBody.create(MediaType.parse("text/plain"), Constants.GRANTYPE)

    sealed class StateLiveData {
        object PreCall : StateLiveData()
        class RefreshStateUi(val response: ModelResponseAccessToken) : StateLiveData()
        object PostCall : StateLiveData()
    }



    fun getAccessToken(code: String) {
        val code: RequestBody = RequestBody.create(MediaType.parse("text/plain"), code)
        launch {

            _modelAccessToken.value = StateLiveData.PreCall
             when (val result = accessTokenRepository
                 .getAccessToken(clientId, clientSecret, redirectUri, grantType, code)) {
                is ResultWrapper.Success -> {
                    _modelAccessToken.value = StateLiveData.RefreshStateUi(result.value)
                }
                is ResultWrapper.NetworkError -> {
                    Log.d("Test", result.throwable.message())
                }
                is ResultWrapper.GenericError -> {
                    Log.d("Test", result.error)
                }
            }
            _modelAccessToken.value = StateLiveData.PostCall
        }

    }

    init { job = SupervisorJob() }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}