package com.example.socialnetwork.ui.twitter

import android.media.MediaController2
import android.media.session.MediaController
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.socialnetwork.R
import com.example.socialnetwork.utils.*
import kotlinx.android.synthetic.main.fragment_media_twitter.*
import twitter4j.ControlStreamInfo


class MediaTwitterFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media_twitter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments?.let { MediaTwitterFragmentArgs.fromBundle(it) }

        val media = args?.mediaUrl?.split(",")
        val mediaType = media?.get(1)
        val mediaUrl = media?.get(0)

        Log.i("Carpul", "type: $mediaType")
        Log.i("Carpul", "url: $mediaUrl")

        if (mediaType != "video") {

            Glide.with(requireContext())
                .load(mediaUrl)
                .into(imageFullScreenTweet)
            imageFullScreenTweet.show()
            videoView.hide()

        } else {

            val controller = android.widget.MediaController(requireContext())
            controller.setAnchorView(videoView)
            videoView.setMediaController(controller)
            videoView.setVideoPath(mediaUrl)
            videoView.start()
            videoView.show()
            imageFullScreenTweet.hide()
        }

    }
}