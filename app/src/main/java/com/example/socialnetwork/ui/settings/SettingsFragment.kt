package com.example.socialnetwork.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.socialnetwork.MainActivity
import com.example.socialnetwork.R
import com.facebook.login.LoginManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private val loginManager: LoginManager= LoginManager.getInstance()
    private lateinit var toolBar: MaterialToolbar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        toolBar = (activity as MainActivity).toolBar

        toolBar.title = getString(R.string.titleSetting)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logoutButtonFacebook.setOnClickListener {
            loginManager.logOut()
            findNavController().apply {
                popBackStack()
                navigate(R.id.loginTwitterFragment)
            }
        }

        logoutButtonTwitter.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().apply {
                popBackStack()
                navigate(R.id.loginTwitterFragment)
            }
        }
    }

}