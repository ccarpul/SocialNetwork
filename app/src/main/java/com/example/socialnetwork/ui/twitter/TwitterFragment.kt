package com.example.socialnetwork.ui.twitter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.socialnetwork.MainActivity
import com.example.socialnetwork.R
import com.example.socialnetwork.adapter.AdapterRecyclerTwitter
import com.example.socialnetwork.utils.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_twitter.*
import kotlinx.android.synthetic.main.navigation_header.*
import kotlinx.android.synthetic.main.profile_style.*
import kotlinx.android.synthetic.main.profile_style.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class TwitterFragment : Fragment(), AdapterRecyclerTwitter.OnClickImageTweet,
    AdapterRecyclerTwitter.OnclickTweet {

    private val twitterViewModel: TwitterViewModel by viewModel()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapterRecycler: AdapterRecyclerTwitter =
        AdapterRecyclerTwitter(arrayListOf(), this)
    private var userToken: String? = null
    private var userTokenSecret: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (auth.currentUser == null) {
            findNavController().apply {
                popBackStack()
                navigate(R.id.welcomeFragment)
            }
        }
        getTokens()
        twitterViewModel.modelTwitter.observe(this, Observer(::upDateUi))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_twitter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupToolbar()
        setupNavigationView()
        onScrollTwitter()

        swipeRefreshTwitter.setOnRefreshListener {
            twitterViewModel.page = 1
            adapterRecycler.cleanList()
            twitterViewModel.getData(userToken, userTokenSecret)
        }
    }

    private fun upDateUi(state: TwitterViewModel.StateLiveData) {
        when (state) {
            is TwitterViewModel.StateLiveData.InitialStateUi -> {
                twitterViewModel.getData(userToken, userTokenSecret)
            }
            is TwitterViewModel.StateLiveData.PreCall -> {
                progressBarTw.show()
            }
            is TwitterViewModel.StateLiveData.RefreshStateUi -> {
                adapterRecycler.addData(state.response)
            }
            is TwitterViewModel.StateLiveData.PostCall -> {
                progressBarTw.hide()
                swipeRefreshTwitter.isRefreshing = false
            }
            is TwitterViewModel.StateLiveData.AdapterRecycler -> {
                adapterRecycler.addData(state.dataRecyclerView)
            }
        }
    }

    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        twitterRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = adapterRecycler
        }
    }

    private fun getTokens() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        userToken = sharedPref.getString("userToken", null)
        userTokenSecret = sharedPref.getString("userTokenSecret", null)

    }

    private fun setupToolbar() {

        (activity as MainActivity).toolbar.setupToolbar(
            imageProvider=R.drawable.ic_twitter_white,
            textUserName = auth.currentUser?.displayName,
            textScreenName = auth.currentUser?.email,
            imageNavigationIcon = auth.currentUser?.photoUrl.toString()
        )
    }

    private fun setupNavigationView() {

        (activity as MainActivity).drawer.navDrawer.setupMenuItem(
            id = R.id.twitterFragment,
            titleMenu = getString(R.string.twitter),
            isSelected = true)

        (activity as MainActivity).headerNavigationView.setupHeaderNav(
            textHeader = auth.currentUser?.displayName ?: getString(R.string.app_name),
            imageHeaderUri = auth.currentUser?.photoUrl.toString().getClearImageUrl()
        )
    }

    private fun onScrollTwitter() {
        twitterRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                twitterViewModel.pos = adapterRecycler.getPosition()
                if (twitterRecyclerView.isLastArticleDisplayed(linearLayoutManager)) {
                    twitterViewModel.page++
                    twitterViewModel.getData(userToken, userTokenSecret)

                }
            }
        })
    }

    override fun onClickImageTweet(mediaUrl: String?, mediaType: String?) {

        val passMediaUrl
                = TwitterFragmentDirections.actionTwitterFragmentToMediaTwitterFragment()
            .setMediaUrl("$mediaUrl,$mediaType")
        findNavController().navigate(passMediaUrl)

        (activity as MainActivity).toolbar.hide()
    }

    override fun onclickTweet(tweetUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl))
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        (activity as MainActivity).drawer.navDrawer.setupMenuItem(
            id = R.id.twitterFragment,
            titleMenu = getString(R.string.twitter),
            isSelected = false)

    }
}