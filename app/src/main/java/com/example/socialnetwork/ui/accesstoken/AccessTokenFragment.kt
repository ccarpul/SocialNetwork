package com.example.socialnetwork.ui.accesstoken

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.socialnetwork.*
import com.example.socialnetwork.utils.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_accesstoken_ig.*
import kotlinx.android.synthetic.main.navigation_header.*
import org.koin.android.viewmodel.ext.android.viewModel

class AccessTokenFragment : Fragment(), AccessTokenListener {

    private val accessTokenViewModel: AccessTokenViewModel by viewModel()
    private lateinit var listener: AccessTokenListener
    private lateinit var sharedPref: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).toolbar.gone()

        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        accessTokenViewModel.modelAccessToken.observe(this, Observer(::upDateUi))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_accesstoken_ig, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarAccessIg.show()
        listener = this
        setupWebView()

    }

    override fun onCodeReceived(auth_code: String?) {
        
        Log.i("Carpul", "onCodeReceived: $auth_code")
        when (auth_code) {
            "error" -> Log.i("Carpul", "onCodeReceived: $auth_code")
            "denied" -> {
                webView.hide()
                findNavController().navigate(R.id.action_loginInstagramFragment_to_welcomeFragment)
            }
            null -> if(progressBarAccessIg != null) progressBarAccessIg.hide()
            else -> {
                accessTokenViewModel.getAccessToken(auth_code)
            }
        }
    }

    private fun upDateUi(state: AccessTokenViewModel.StateLiveData) {
        when (state) {

            is AccessTokenViewModel.StateLiveData.PreCall -> progressBarAccessIg.show()

            is AccessTokenViewModel.StateLiveData.RefreshStateUi -> {

                with(sharedPref.edit()) {
                    putString("accessTokenInstagram", state.response.access_token)
                    commit()
                }
            }
            is AccessTokenViewModel.StateLiveData.PostCall -> {

                progressBarAccessIg.hide()
                findNavController().navigate(R.id.action_loginInstagramFragment_to_instagramFragment)
            }
        }
    }

    private fun setupWebView() {
        webView.apply {
            webViewClient = MyWebViewClient(listener)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            loadUrl(Constants.URL_INSTAGRAM_AUTH)
        }
    }

}