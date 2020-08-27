package com.example.socialnetwork.ui.twitter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialnetwork.data.model.ModelResponseTwitter
import com.example.socialnetwork.utils.Constants
import com.example.socialnetwork.utils.ResultWrapper
import kotlinx.coroutines.*
import twitter4j.Paging
import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder
import kotlin.coroutines.CoroutineContext

class TwitterViewModel(private val twitterRespository: TwitterRepository) : ViewModel(),
    CoroutineScope {

    private var job: Job = Job()
    var page = 1
    var pos = 0

    private  lateinit var dataRestorerRecycler: ResponseList<Status>

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    sealed class StateLiveData {
        object InitialStateUi : StateLiveData()
        object PreCall        : StateLiveData()
        object PostCall       : StateLiveData()
        class RefreshStateUi(val response: List<Status>)         : StateLiveData()
        class AdapterRecycler(val dataRecyclerView: List<Status>): StateLiveData()
    }

    private val uiModelTwitter = MutableLiveData<StateLiveData>()
    val modelTwitter: LiveData<StateLiveData>
        get() {
            if (uiModelTwitter.value == null) uiModelTwitter.value = StateLiveData.InitialStateUi
            else  uiModelTwitter.value = StateLiveData.AdapterRecycler(dataRestorerRecycler)
            return uiModelTwitter
        }

    fun getData(userToken: String?, userTokenSecret: String?) {
        launch {

            uiModelTwitter.value = StateLiveData.PreCall
            when(val result =
                twitterRespository.getTimeLineTwitter(userToken, userTokenSecret, page)){
                is ResultWrapper.Success -> {
                    uiModelTwitter.value = StateLiveData.RefreshStateUi(result.value)
                }
                is ResultWrapper.GenericError -> {
                    Log.d("Test", result.error.toString())
                }
                is ResultWrapper.NetworkError -> {
                    Log.d("Test", result.throwable.message())
                }
            }
            uiModelTwitter.value = StateLiveData.PostCall
        }
    }

    init {

        job = SupervisorJob()
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}