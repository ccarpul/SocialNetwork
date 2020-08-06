package com.example.socialnetwork.ui.login

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socialnetwork.AuthResult
import com.example.socialnetwork.data.model.ModelResponse
import com.example.socialnetwork.myResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthCredential
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel(), CoroutineScope {

    private lateinit var resultTwitter: Task<AuthResult>
    private val _loginResult = MutableLiveData<StateLiveData>()
    val loginResult: LiveData<StateLiveData> = _loginResult

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    sealed class StateLiveData {
        object PreLogin : StateLiveData()
        class RefreshUi(val result: myResult<Any>, val token:String) : StateLiveData()
        object PostLogin : StateLiveData()
    }

    fun login(
        action: LoginFragment.ActionFireBase,
        activity: FragmentActivity?
    ) {

        launch {
            _loginResult.value = StateLiveData.PreLogin

            try {
                when (action) {

                    LoginFragment.ActionFireBase.TWITTER -> {
                        resultTwitter = loginRepository.setLoginByTwitter(activity)
                            .addOnCompleteListener {
                                val oAuthCredential = it.result
                                    ?.credential as OAuthCredential
                                val idToken = oAuthCredential.idToken
                                val accessToken = oAuthCredential.accessToken
                                val secret = oAuthCredential.secret

                                Log.i("Carpul", "login: $accessToken")
                                Log.i("Carpul", "login: $secret")

                                _loginResult.value = it.AuthResult(accessToken, "twitter")
                                _loginResult.value = StateLiveData.PostLogin

                            }.addOnFailureListener {
                                StateLiveData.RefreshUi(myResult.GenericError(it.localizedMessage), "")
                            }
                    }
                    LoginFragment.ActionFireBase.FACEBOOK -> {
                    }
                }
            } catch (e: Exception) {
                _loginResult.value =
                    StateLiveData.RefreshUi(myResult.GenericError(e.localizedMessage), "")
                _loginResult.value = StateLiveData.PostLogin
            }
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