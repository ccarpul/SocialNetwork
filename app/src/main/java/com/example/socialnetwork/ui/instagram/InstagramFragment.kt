package com.example.socialnetwork.ui.instagram

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.*
import com.example.socialnetwork.adapter.AdapterRecyclerInstagram
import com.example.socialnetwork.utils.*
import com.facebook.Profile
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_instagram.*
import kotlinx.android.synthetic.main.navigation_header.*
import kotlinx.android.synthetic.main.profile_style.*
import kotlinx.android.synthetic.main.profile_style.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class InstagramFragment : Fragment() {

    private val instagramViewModel: InstagramViewModel by viewModel()

    private lateinit var toolbar: MaterialToolbar
    private lateinit var userNameToolbar: TextView
    private lateinit var userScreenNameToolbar: TextView
    private lateinit var imageNavigationHeader: AppCompatImageView
    private lateinit var textHeaderTitle: TextView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapterRecycler: AdapterRecyclerInstagram = AdapterRecyclerInstagram(arrayListOf())
    var code = ""


    override fun onAttach(context: Context) {
        super.onAttach(context)
        toolbar               = (activity as MainActivity).toolbar
        userNameToolbar       = toolbar.userNameToolbar
        userScreenNameToolbar = toolbar.screenNameToolbar
        imageNavigationHeader = (activity as MainActivity).imageHeadNavigation
        textHeaderTitle       = (activity as MainActivity).textHeaderTitle

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        code = sharedPref.getString("accessTokenInstagram","") ?: ""

        if(code == "") findNavController().navigate(R.id.welcomeFragment)

        instagramViewModel.modelInstagram.observe(this, Observer(::upDateUi))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_instagram, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        onScrollTopNews()
    }

    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(requireContext())
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
                    if (instagramViewModel.mediaCount - instagramViewModel.pos >= 4)
                        instagramViewModel.getData(code)
                    else makeToast(
                        requireContext(),
                        getString(R.string.endList)
                    )
                }
            }
        })
    }

    private fun upDateUi(state: InstagramViewModel.StateLiveData) {
        when (state) {
            is InstagramViewModel.StateLiveData.InitialStateUi -> {
                instagramViewModel.getData(code)
                instagramViewModel.getProfile(code)
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

                setupToolbar(state.response.username)
                setupNavigationView(state.response.username)
            }
            is InstagramViewModel.StateLiveData.AdapterRecycler -> {
                for (data in state.dataRecyclerView)
                    adapterRecycler.addData(data)
            }
            is InstagramViewModel.StateLiveData.ErrorResponse -> {
                makeToast(requireContext(), "Conection Error, please try again")
                findNavController().navigate(R.id.loginInstagramFragment)
            }

        }
    }

    private fun setupToolbar(userName:  String){

        (activity as MainActivity).toolbar.show()

        (activity as MainActivity).imageProviderToolbar.apply {
            setImageResource(R.drawable.ic_instagram_white)
            show()
        }

        userNameToolbar.text = "@$userName"
        userScreenNameToolbar.gone()
        toolbar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_hamburger_24)

        toolbar.show()
    }

    private fun setupNavigationView(userName: String) {
        textHeaderTitle.text = " @$userName"
        imageNavigationHeader.setImageResource(R.mipmap.ic_launcher)
    }
}