package com.example.socialnetwork.ui.instagram

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.*
import com.example.socialnetwork.adapter.AdapterRecyclerView
import com.facebook.AccessToken
import com.facebook.Profile
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_instagram.*
import kotlinx.android.synthetic.main.fragment_twitter.*
import kotlinx.android.synthetic.main.login_twitter.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import org.jetbrains.annotations.NotNull
import org.koin.android.viewmodel.ext.android.viewModel

class InstagramFragment : Fragment(), AdapterRecyclerView.OnClickSelectedItem {

    private val instagramViewModel: InstagramViewModel by viewModel()

    private lateinit var toolBar: MaterialToolbar
    private var accessToken: AccessToken? = null
    private var profile: Profile? = null
    private var isLoggedIn = false
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapterRecycler: AdapterRecyclerView = AdapterRecyclerView(arrayListOf(), this)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        toolBar = (activity as MainActivity).toolBar

        try {
            accessToken = AccessToken.getCurrentAccessToken()
            profile = Profile.getCurrentProfile()
            isLoggedIn = accessToken != null

        } catch (e: Exception) {
            Log.i("Carpul", "$e")
            findNavController().navigate(R.id.loginTwitterFragment)
        }

        if (!isLoggedIn) findNavController().navigate(R.id.loginTwitterFragment)

        instagramViewModel.modelInstagram.observe(this, Observer(::upDateUi))
        toolBar.title = "Social NetWork of ${profile?.firstName}"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instagram, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        onScrollTopNews()
    }

    private fun setupRecyclerView() {
        linearLayoutManager = GridLayoutManager(requireContext(), 2)
        instagramRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = adapterRecycler
        }

    }

    private fun onScrollTopNews() {
        instagramRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                instagramViewModel.pos = adapterRecycler.getPosition()
                if (instagramRecyclerView.isLastArticleDisplayed(linearLayoutManager)) {
                    Log.i("Carpul", "${instagramViewModel.mediaCount - instagramViewModel.pos}")

                    if (instagramViewModel.mediaCount - instagramViewModel.pos >= 4)
                        instagramViewModel.getData()
                    else makeToast(requireContext(), "Es todo")
                }
            }
        })
    }

    private fun upDateUi(state: InstagramViewModel.StateLiveData) {
        when (state) {
            is InstagramViewModel.StateLiveData.InitialStateUi -> {
                instagramViewModel.getData()
                instagramViewModel.getProfile()
            }
            is InstagramViewModel.StateLiveData.PreCall -> {
                progressBarIg.show()
            }
            is InstagramViewModel.StateLiveData.RefreshStateUi -> {

                adapterRecycler.addData(state.response)
            }
            is InstagramViewModel.StateLiveData.PostCall -> {
                progressBarIg.hide()
            }
            is InstagramViewModel.StateLiveData.RefreshStateProfile -> {
                userName.text = "@${state.response.username}"
            }
        }
    }

    override fun onClick(query: String) {
        Log.i("Carpul", "onClick: query")
    }


}