package com.example.socialnetwork.ui.twitter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialnetwork.ResultWrapper
import com.example.socialnetwork.data.model.ModelTwitter
import com.example.socialnetwork.data.model.ProfileInstagram
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class TwitterViewModel(private val twitterRespository: TwitterRepository) : ViewModel(),
    CoroutineScope {

    private var job: Job = Job()


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    /**Sobreesribiendo la variable coroutineContext para usar la
    // interfaz CourutinaScope como ambito de las coroutinas del ViewModel
    // Especifico el dispacher para que se ejecute en el hilo principal y
    //le paso la referencia job para controlar el estado de la corrutina*/
    sealed class StateLiveData {
        object InitialStateUi : StateLiveData()
        object PreCall : StateLiveData()
        class RefreshStateUi(val response: ModelTwitter) : StateLiveData()
        class RefreshStateProfile(val response: ModelTwitter) : StateLiveData()
        object PostCall : StateLiveData()
    }

    private val uiModelTwitter = MutableLiveData<StateLiveData>()
    val modelTwitter: LiveData<StateLiveData>
        get() {
            if (uiModelTwitter.value == null) uiModelTwitter.value =
                StateLiveData.InitialStateUi
            return uiModelTwitter
        }

    fun getData() {
        Log.i("Carpul", "getData: ")
        launch {

            uiModelTwitter.value = StateLiveData.PreCall

            when (val result = twitterRespository.getDataTwitter()) {

                is ResultWrapper.Success -> {
                    uiModelTwitter.value = StateLiveData.RefreshStateUi(result.value)

                    if (result.value[0].user.screen_name != null)
                        Log.i(
                            "Carpul",
                            "getData: "
                        )
                    else
                        Log.i(
                            "Carpul",
                            "getData: result.value.user[0].screen_namer"
                        )

                }
                is ResultWrapper.NetworkError -> {
                    Log.d("Test", result.throwable.message())
                }
                is ResultWrapper.GenericError -> {
                    Log.d("Test", result.error)
                }
            }
            uiModelTwitter.value = StateLiveData.PostCall
            Log.i("Carpul", "PostCall")
        }

    }

    init {
        job = SupervisorJob()
    }

    override fun onCleared() {  //Metodo de la clase View Model, al cerrarse el View Model
        // se cierran todas las corrutinas pertenecientes ael presente Scope
        job.cancel()
        super.onCleared()
    }
}