package com.example.socialnetwork.ui.twitter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialnetwork.MainActivity
import com.example.socialnetwork.R
import com.example.socialnetwork.adapter.AdapterRecyclerTwitter
import com.example.socialnetwork.utils.hide
import com.example.socialnetwork.utils.show
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_instagram.userName
import kotlinx.android.synthetic.main.fragment_twitter.*
import kotlinx.android.synthetic.main.profile_style.*
import org.koin.android.viewmodel.ext.android.viewModel

class TwitterFragment : Fragment() {

    private val twitterViewModel: TwitterViewModel by viewModel()

    private lateinit var toolBar: MaterialToolbar
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapterRecycler: AdapterRecyclerTwitter = AdapterRecyclerTwitter(arrayListOf())
    private var userToken: String? = ""
    private var userTokenSecret: String? = ""

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_twitter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()
        toolBar = (activity as MainActivity).toolBar
        toolBar.show()

        val user = auth.currentUser?.displayName
        toolBar.title = getString(R.string.TitleSocialNetwork) + user
        userName.text = user ?: getString(R.string.twitter)
         if( auth.currentUser?.photoUrl != null) {
             Picasso.with(requireContext()).load(auth.currentUser?.photoUrl)
                 .placeholder(R.mipmap.ic_launcher_foreground)
                 .resize(40, 40)
                 .centerCrop()
                 .into(userPicture)
         }else userPicture.setImageDrawable(resources.getDrawable(R.mipmap.ic_launcher))

    }
    fun upDateUi(state: TwitterViewModel.StateLiveData){
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
            }
            is TwitterViewModel.StateLiveData.RefreshStateProfile -> {
                userName.text = "@${state.response}"
            }
            is TwitterViewModel.StateLiveData.AdapterRecycler -> {
                for (data in state.dataRecyclerView)
                    adapterRecycler.addData(data)
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
        userToken = sharedPref.getString("userToken","")
        userTokenSecret = sharedPref.getString("userTokenSecret", "")
    }
}