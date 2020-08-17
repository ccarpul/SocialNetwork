package com.example.socialnetwork.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.socialnetwork.MainActivity
import com.example.socialnetwork.R
import com.example.socialnetwork.utils.hide
import com.example.socialnetwork.utils.makeToast
import com.google.android.gms.tasks.Task
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userToken: String? = null
    private var userTokenSecret: String? = null
    private lateinit var toolBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBar = (activity as MainActivity).toolBar
        toolBar.hide()

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        textViewTwitter.setOnClickListener {
            if (firebaseAuth.currentUser == null) {

                setLoginByTwitter().addOnCompleteListener {

                    val oAuthCredential = it.result?.credential as OAuthCredential

                    with(sharedPref.edit()) {
                        putString("userToken", oAuthCredential.accessToken)
                        putString("userTokenSecret", oAuthCredential.secret)
                        commit()
                    }

                    val passTokens =
                        WelcomeFragmentDirections.actionWelcomeFragmentToTwitterFragment()
                            .setUserToken("$userToken,$userTokenSecret")
                    findNavController().navigate(passTokens)

                }.addOnFailureListener {
                    makeToast(
                        requireContext(),
                        it.localizedMessage ?: "Try again"
                    )
                }
            } else {

                userToken = sharedPref.getString("userToken", "")
                userTokenSecret = sharedPref.getString("userTokenSecret", "")
                val passTokens = WelcomeFragmentDirections.actionWelcomeFragmentToTwitterFragment()
                    .setUserToken("$userToken,$userTokenSecret")
                findNavController().navigate(passTokens)
                Log.i("Carpul", "onViewCreated: $userToken , $userTokenSecret")

            }
        }
    }

    fun setLoginByTwitter(): Task<AuthResult> {

        val pendingAuthResult = firebaseAuth.pendingAuthResult
        return if (pendingAuthResult != null) {
            pendingAuthResult
        } else {

            val oAuthProvider = OAuthProvider.newBuilder("twitter.com")
            firebaseAuth.startActivityForSignInWithProvider(
                requireActivity(),
                oAuthProvider.build()
            )
                .addOnSuccessListener {

                }
        }
    }

}