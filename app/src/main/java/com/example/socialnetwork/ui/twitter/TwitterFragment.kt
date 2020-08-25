package com.example.socialnetwork.ui.twitter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
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
import com.example.socialnetwork.utils.getClearImageUrl
import com.example.socialnetwork.utils.hide
import com.example.socialnetwork.utils.isLastArticleDisplayed
import com.example.socialnetwork.utils.show
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_instagram.userName
import kotlinx.android.synthetic.main.fragment_twitter.*
import kotlinx.android.synthetic.main.navigation_header.*
import kotlinx.android.synthetic.main.profile_style.*
import kotlinx.android.synthetic.main.profile_style.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class TwitterFragment : Fragment(), AdapterRecyclerTwitter.OnClickImageTweet {

    private val twitterViewModel: TwitterViewModel by viewModel()
    private lateinit var toolbar: MaterialToolbar
    private lateinit var userNameToolbar: TextView
    private lateinit var userScreenNameToolbar: TextView
    private lateinit var imageNavigationHeader: AppCompatImageView
    private lateinit var textHeaderTitle: TextView
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapterRecycler: AdapterRecyclerTwitter =
        AdapterRecyclerTwitter(arrayListOf(), this)
    private var userToken: String? = ""
    private var userTokenSecret: String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        toolbar = (activity as MainActivity).toolbar
        userNameToolbar = toolbar.userNameToolbar
        userScreenNameToolbar = toolbar.screenNameToolbar
        imageNavigationHeader = (activity as MainActivity).imageHeadNavigation
        textHeaderTitle = (activity as MainActivity).textHeaderTitle

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
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
            is TwitterViewModel.StateLiveData.RefreshStateProfile -> {
                userName.text = "@${state.response}"
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
        userToken = sharedPref.getString("userToken", "")
        userTokenSecret = sharedPref.getString("userTokenSecret", "")

    }

    private fun setupToolbar() {
        (activity as MainActivity).imageProviderToolbar.apply {
            setImageResource(R.drawable.ic_twitter_white)
            show()
        }
        userNameToolbar.text = auth.currentUser?.displayName
        if (auth.currentUser?.email == "") userScreenNameToolbar.hide()
        else userScreenNameToolbar.apply {
            text = auth.currentUser?.email
            show()
        }

        if (auth.currentUser?.photoUrl != null) {
            Glide.with(toolbar.context).asBitmap()
                .load(auth.currentUser?.photoUrl.toString().getClearImageUrl())
                .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                .into(object : SimpleTarget<Bitmap>(140, 140) {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        toolbar.navigationIcon = BitmapDrawable(resources, resource)
                    }
                })
        } else toolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_hamburger_24)

        toolbar.show()
    }

    private fun setupNavigationView() {

        textHeaderTitle.text = auth.currentUser?.displayName ?: getString(R.string.app_name)
        Glide.with(requireActivity())
            .load(auth.currentUser?.photoUrl.toString().getClearImageUrl())
            .apply(RequestOptions.bitmapTransform(RoundedCorners(200)))
            .placeholder(R.mipmap.ic_launcher_foreground)
            .into(imageNavigationHeader)
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

    override fun onClick(mediaUrl: String?, mediaType: String?) {

        val passMediaUrl
                = TwitterFragmentDirections.actionTwitterFragmentToMediaTwitterFragment()
            .setMediaUrl("$mediaUrl,$mediaType")
        findNavController().navigate(passMediaUrl)
        toolbar.hide()
    }
}