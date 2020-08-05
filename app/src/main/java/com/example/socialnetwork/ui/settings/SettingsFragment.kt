package com.example.socialnetwork.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.socialnetwork.R
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private val loginManager: LoginManager= LoginManager.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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