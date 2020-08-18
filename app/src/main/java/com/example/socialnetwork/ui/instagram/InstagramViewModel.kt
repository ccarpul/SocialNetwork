package com.example.socialnetwork.ui.instagram

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialnetwork.data.model.ModelResponse
import com.example.socialnetwork.data.model.ProfileInstagram
import com.example.socialnetwork.utils.ResultWrapper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class InstagramViewModel(private val instagramRespository: InstagramRespository): ViewModel(), CoroutineScope {

    private var job: Job = Job()
    var pos = 0
    var mediaCount=0
    var after =""
    private val dataRestaurerRecycler = arrayListOf<ModelResponse>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    sealed class StateLiveData{
        object InitialStateUi: StateLiveData()
        object PreCall:        StateLiveData()
        class  RefreshStateUi(val response: ModelResponse) : StateLiveData()
        class  RefreshStateProfile(val response: ProfileInstagram) : StateLiveData()
        object PostCall:        StateLiveData()
        class AdapterRecycler(val dataRecyclerView: ArrayList<ModelResponse>): InstagramViewModel.StateLiveData()
    }

    private val uiModelInstagram = MutableLiveData<StateLiveData>()
    val modelInstagram: LiveData<StateLiveData>
        get() {
            if (uiModelInstagram.value == null) uiModelInstagram.value = StateLiveData.InitialStateUi
            else  uiModelInstagram.value = StateLiveData.AdapterRecycler(dataRestaurerRecycler)
            return uiModelInstagram
        }

    fun getData(code: String) {

        launch {

            uiModelInstagram.value = StateLiveData.PreCall
            when (val result =  instagramRespository.getDataInstagram(after, code)) {
                is ResultWrapper.Success      -> {
                    after = if (mediaCount - pos >= 4) result.value.paging.cursors.after else ""
                    uiModelInstagram.value = StateLiveData.RefreshStateUi(result.value)
                    dataRestaurerRecycler.add(result.value)
                }
                is ResultWrapper.NetworkError -> { Log.d("Test", result.throwable.message()) }
                is ResultWrapper.GenericError -> { Log.d("Test", result.error) }
            }
            uiModelInstagram.value = StateLiveData.PostCall
        }
    }

    fun getProfile(code: String) {

        launch {

            uiModelInstagram.value = StateLiveData.PreCall
            when (val result = instagramRespository.getProfileInstagram(code)) {
                is ResultWrapper.Success      -> {
                    uiModelInstagram.value = StateLiveData.RefreshStateProfile(result.value)
                    mediaCount = result.value.media_count
                }
                is ResultWrapper.NetworkError -> { Log.d("Test", result.throwable.message()) }
                is ResultWrapper.GenericError -> { Log.d("Test", result.error) }
            }
            uiModelInstagram.value = StateLiveData.PostCall
        }
    }

    init { job = SupervisorJob() }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}