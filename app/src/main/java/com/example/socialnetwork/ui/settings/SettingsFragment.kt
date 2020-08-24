package com.example.socialnetwork.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.socialnetwork.MainActivity
import com.example.socialnetwork.R
import com.example.socialnetwork.utils.hide
import com.example.socialnetwork.utils.show
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.profile_style.*
import kotlinx.android.synthetic.main.recycler_style_twitter.*

class SettingsFragment : Fragment() {

    private lateinit var userNameToolbar: TextView
    private lateinit var sharedPref: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)

        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).toolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_hamburger_24)
        (activity as MainActivity).imageProviderToolbar.hide()
        (activity as MainActivity).screenNameToolbar.hide()
        userNameToolbar = (activity as MainActivity).userNameToolbar
        userNameToolbar.text = getString(R.string.titleSetting)

        logoutButtonFacebook.setOnClickListener {
            with(sharedPref.edit()){
                putString("accessTokenInstagram", "")
                commit()
            }

            findNavController().apply {
                popBackStack()
                navigate(R.id.welcomeFragment)
            }
        }

        logoutButtonTwitter.setOnClickListener {
            Firebase.auth.signOut()

            with(sharedPref.edit()){
                putString("userToken", "")
                putString("userTokenSecret", "")
                commit()
            }
            findNavController().apply {
                popBackStack()
                navigate(R.id.welcomeFragment)
            }
        }
    }

}