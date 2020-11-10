package com.example.socialnetwork.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.socialnetwork.MainActivity
import com.example.socialnetwork.R
import com.example.socialnetwork.utils.setupMenuItem
import com.example.socialnetwork.utils.setupToolbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    private lateinit var sharedPref: SharedPreferences.Editor

    override fun onAttach(context: Context) {
        super.onAttach(context)

        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)?.edit() ?: return
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).drawer.navDrawer.setupMenuItem(
            id = R.id.settingsFragment,
            titleMenu = getString(R.string.settings),
            isSelected = true)


        (activity as MainActivity).toolbar.setupToolbar(
            imageProviderVisible = View.GONE,
            screenVisible = View.GONE,
            textUserName = getString(R.string.titleSetting)
        )


        logoutInstagram.setOnClickListener {
            with(sharedPref){
                putString("accessTokenInstagram", null)
                commit()
            }

            findNavController().apply {
                navigate(R.id.action_settingsFragment_to_welcomeFragment)
            }
        }

        logoutTwitter.setOnClickListener {
            Firebase.auth.signOut()

            with(sharedPref){
                putString("userToken", null)
                putString("userTokenSecret", null)
                commit()
            }
            findNavController().apply {
                navigate(R.id.action_settingsFragment_to_welcomeFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).drawer.navDrawer.setupMenuItem(
            id = R.id.settingsFragment,
            titleMenu = getString(R.string.settings),
            isSelected = false)
    }
}