package com.example.socialnetwork.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.socialnetwork.*
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.login_twitter.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment: Fragment() {

    private val loginViewModel: LoginViewModel by viewModel()

    private val callbackManager = CallbackManager.Factory.create()


    enum class ActionFireBase { TWITTER, FACEBOOK }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginViewModel.loginResult.observe(this, Observer {

            when (it) {
                is LoginViewModel.StateLiveData.PreLogin -> progressbarLayout.show()
                is LoginViewModel.StateLiveData.RefreshUi -> {
                    when (it.result) {
                        is myResult.Success -> {
                            findNavController().apply {
                                popBackStack()
                                if(it.result.value == "twitter")
                                    navigate(R.id.twitterFragment)
                            }
                        }
                        is myResult.GenericError -> {
                            Log.i("Carpul", "onAttach: ${it.result.error}")
                            firebaseErrors(it.result.error.toString(), requireContext())
                        }
                    }
                }
                is LoginViewModel.StateLiveData.PostLogin -> progressbarLayout.hide()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_twitter, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonFacebook.setOnClickListener {


            buttonFacebook.setReadPermissions("email")
            buttonFacebook.fragment = this
            buttonFacebook.registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {

                    override fun onSuccess(result: LoginResult) {
                        findNavController().navigate(R.id.instagramFragment)
                    }

                    override fun onCancel() {}

                    override fun onError(error: FacebookException?) {
                        LoginViewModel.StateLiveData.RefreshUi(myResult.GenericError(error?.message))
                    }
                })
        }

        textViewTwitter.setOnClickListener {
            loginViewModel.login(LoginFragment.ActionFireBase.TWITTER, activity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}