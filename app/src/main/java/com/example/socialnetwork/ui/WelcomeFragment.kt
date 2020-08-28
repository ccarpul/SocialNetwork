package com.example.socialnetwork.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.socialnetwork.MainActivity
import com.example.socialnetwork.R
import com.example.socialnetwork.utils.Constants
import com.example.socialnetwork.utils.hide
import com.example.socialnetwork.utils.makeToast
import com.example.socialnetwork.utils.show
import com.google.android.gms.tasks.Task
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var pendingAuthResult: Task<AuthResult>? = null
    private var userToken: String? = null
    private var userTokenSecret: String? = null
    private lateinit var sharedPref: SharedPreferences
    private var accessTokenInstagram: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        firebaseAuth = FirebaseAuth.getInstance()
        pendingAuthResult = firebaseAuth.pendingAuthResult
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        accessTokenInstagram = sharedPref.getString("accessTokenInstagram", "").toString()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).toolbar.hide()

        gotoInstagram.setOnClickListener {

            if (accessTokenInstagram == "") {
                val passUrl
                        = WelcomeFragmentDirections
                    .actionWelcomeFragmentToLoginInstagramFragment(Constants.URL_INSTAGRAM_AUTH)
                findNavController().navigate(passUrl)
            } else {
                findNavController().navigate(R.id.action_welcomeFragment_to_instagramFragment)
            }
        }

        gotoTwitter.setOnClickListener {

            progressBarWelcome.show()
            if (FirebaseAuth.getInstance().currentUser == null) {
                setLoginByTwitter().addOnCompleteListener {

                    try {
                        val oAuthCredential = it.result?.credential as OAuthCredential
                        with(sharedPref.edit()) {
                            putString("userToken", oAuthCredential.accessToken)
                            putString("userTokenSecret", oAuthCredential.secret)
                            commit()
                        }
                        progressBarWelcome.hide()
                        findNavController().navigate(R.id.twitterFragment)
                    }catch (e: Exception){
                        Log.i("Carpul", "onViewCreated: $e")
                    }
                }.addOnFailureListener {
                    progressBarWelcome.hide()
                    makeToast(
                        requireContext(),
                        it.localizedMessage ?: getString(R.string.try_again)
                    )
                }.addOnCanceledListener { Log.i("Carpul", "onViewCreated: Cancelado") }
            } else {
                val passTokens = WelcomeFragmentDirections.actionWelcomeFragmentToTwitterFragment()
                    .setUserToken("$userToken,$userTokenSecret")
                findNavController().navigate(passTokens)

            }
        }
    }

    private fun setLoginByTwitter(): Task<AuthResult> {
        return if (pendingAuthResult != null) {
            pendingAuthResult!!
        } else {

            val oAuthProvider = OAuthProvider.newBuilder("twitter.com")
            firebaseAuth.startActivityForSignInWithProvider(
                requireActivity(),
                oAuthProvider.build()
            )
        }
    }
}