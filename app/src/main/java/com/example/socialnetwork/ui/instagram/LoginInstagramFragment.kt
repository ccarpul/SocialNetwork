package com.example.socialnetwork.ui.instagram

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.socialnetwork.*
import com.example.socialnetwork.utils.show
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_layout.*

class LoginInstagramFragment: Fragment() {

    private val callbackManager = CallbackManager.Factory.create()
    private lateinit var toolBar: MaterialToolbar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBar = (activity as MainActivity).toolBar
        toolBar.show()
        toolBar.title = getString(R.string.welcome)

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

                    }
                })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}