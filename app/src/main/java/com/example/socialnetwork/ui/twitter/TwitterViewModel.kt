package com.example.socialnetwork.ui.twitter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialnetwork.ResultWrapper
import com.example.socialnetwork.data.model.ModelTwitter
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TwitterViewModel(private val twitterRespository: TwitterRepository) : ViewModel(),
    CoroutineScope {

    private var job: Job = Job()
    var userToken = ""
    private val dataRestaurerRecycler = arrayListOf<ModelTwitter>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    sealed class StateLiveData {
        object InitialStateUi : StateLiveData()
        object PreCall : StateLiveData()
        class RefreshStateUi(val response: ModelTwitter) : StateLiveData()
        class RefreshStateProfile(val response: ModelTwitter) : StateLiveData()
        object PostCall : StateLiveData()
        class AdapterRecycler(val dataRecyclerView: ArrayList<ModelTwitter>): StateLiveData()
    }

    private val uiModelTwitter = MutableLiveData<StateLiveData>()
    val modelTwitter: LiveData<StateLiveData>
        get() {
            if (uiModelTwitter.value == null) uiModelTwitter.value = StateLiveData.InitialStateUi
            else  uiModelTwitter.value = StateLiveData.AdapterRecycler(dataRestaurerRecycler)
            return uiModelTwitter
        }

    fun getData() {
        launch {

            uiModelTwitter.value = StateLiveData.PreCall

            when (val result = twitterRespository.getDataTwitter(userToken)) {

                is ResultWrapper.Success -> {
                    uiModelTwitter.value = StateLiveData.RefreshStateUi(result.value)
                    dataRestaurerRecycler.add(result.value)
                }
                is ResultWrapper.NetworkError -> {
                    Log.d("Test", result.throwable.message())
                }
                is ResultWrapper.GenericError -> {
                    Log.d("Test", result.error)
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