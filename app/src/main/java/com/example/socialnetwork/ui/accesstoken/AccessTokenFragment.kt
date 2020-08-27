package com.example.socialnetwork.ui.accesstoken

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.socialnetwork.*
import com.example.socialnetwork.utils.*
import com.facebook.CallbackManager
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login_instagram.*
import kotlinx.android.synthetic.main.navigation_header.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.android.synthetic.main.profile_style.*
import kotlinx.android.synthetic.main.profile_style.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class AccessTokenFragment : Fragment(), AccessTokenListener {

    private val accessTokenViewModel: AccessTokenViewModel by viewModel()
    private val callbackManager = CallbackManager.Factory.create()
    private lateinit var listener: AccessTokenListener
    private lateinit var sharedPref: SharedPreferences


    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        accessTokenViewModel.modelAccessToken.observe(this, Observer(::upDateUi))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_instagram, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupHeaderNavigation()
        listener = this
        setupWebView()
    }

    private fun setupWebView() {
        webView.apply {
            webViewClient = MyWebViewClient(listener)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            loadUrl(Constants.URL_INSTAGRAM_AUTH)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCodeReceived(auth_code: String) {

        if (auth_code != "denied") accessTokenViewModel.getAccessToken(auth_code)
        else findNavController().navigate(R.id.welcomeFragment)
    }

    private fun upDateUi(state: AccessTokenViewModel.StateLiveData) {

        when (state) {

            is AccessTokenViewModel.StateLiveData.PreCall -> {
                //progressBarIg.show()
            }
            is AccessTokenViewModel.StateLiveData.RefreshStateUi -> {

                with(sharedPref.edit()) {
                    putString("accessTokenInstagram", state.response.access_token)
                    commit()
                }
            }
            is AccessTokenViewModel.StateLiveData.PostCall -> {

                //progressBarIg.hide()
                findNavController().navigate(R.id.instagramFragment)
            }
        }
    }
    private fun setupToolbar() {
        (activity as MainActivity).toolbar.setupToolbar(
            visible = View.VISIBLE,
            textUserName = getString(R.string.welcome_to_instagram),
            screenVisible = View.GONE,
            imageProvider = R.drawable.ic_instagram_white)
    }

    private fun setupHeaderNavigation(){
        (activity as MainActivity).headerNavigationView.setupHeaderNav(
            textHeader = getString(R.string.app_name),
            imageHeaderDrawable = R.mipmap.ic_launcher)
    }
}