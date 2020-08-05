package com.example.socialnetwork.ui.instagram

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialnetwork.ResultWrapper
import com.example.socialnetwork.data.model.ModelResponse
import com.example.socialnetwork.data.model.ProfileInstagram
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class InstagramViewModel(private val instagramRespository: InstagramRespository): ViewModel(), CoroutineScope {

    private var job: Job = Job()
    var pos = 0
    var mediaCount=0
    var after =""

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    /**Sobreesribiendo la variable coroutineContext para usar la
    // interfaz CourutinaScope como ambito de las coroutinas del ViewModel
    // Especifico el dispacher para que se ejecute en el hilo principal y
    //le paso la referencia job para controlar el estado de la corrutina*/
    sealed class StateLiveData{
        object InitialStateUi: StateLiveData()
        object PreCall:        StateLiveData()
        class  RefreshStateUi(val response: ModelResponse) : StateLiveData()
        class  RefreshStateProfile(val response: ProfileInstagram) : StateLiveData()
        object PostCall:        StateLiveData()
    }

    private val uiModelInstagram = MutableLiveData<StateLiveData>()
    val modelInstagram: LiveData<StateLiveData>
        get() {
            if (uiModelInstagram.value == null) uiModelInstagram.value = StateLiveData.InitialStateUi
            return uiModelInstagram
        }

    fun getData() {

        launch {

            uiModelInstagram.value = StateLiveData.PreCall
            when (val result =  instagramRespository.getDataInstagram(after)) {
                is ResultWrapper.Success      -> {
                    after = if (mediaCount - pos >= 4) result.value.paging.cursors.after else ""

                    uiModelInstagram.value = StateLiveData.RefreshStateUi(result.value)

                }
                is ResultWrapper.NetworkError -> { Log.d("Test", result.throwable.message()) }
                is ResultWrapper.GenericError -> { Log.d("Test", result.error) }
            }
            uiModelInstagram.value = StateLiveData.PostCall
        }

    }

    fun getProfile() {

        launch {


            uiModelInstagram.value = StateLiveData.PreCall
            when (val result = instagramRespository.getProfileInstagram()) {
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

    override fun onCleared() {  //Metodo de la clase View Model, al cerrarse el View Model
        // se cierran todas las corrutinas pertenecientes ael presente Scope
        job.cancel()
        super.onCleared()
    }
}