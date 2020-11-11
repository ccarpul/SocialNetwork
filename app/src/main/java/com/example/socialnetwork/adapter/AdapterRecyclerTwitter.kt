package com.example.socialnetwork.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialnetwork.R
import com.example.socialnetwork.utils.*
import kotlinx.android.synthetic.main.recycler_style_twitter.view.*
import org.koin.core.definition.indexKey
import twitter4j.Status
import twitter4j.User
import kotlin.math.log


class AdapterRecyclerTwitter(

    private var listUserTweet: MutableList<Status>,
    private val listenerImageTweet: OnClickImageTweet

) : RecyclerView.Adapter<AdapterRecyclerTwitter.AdapterViewHolder>() {

    fun addData(data: List<Status>) {
        listUserTweet.addAll(data)
        originalList = listUserTweet
        notifyDataSetChanged()
    }

    private lateinit var originalList: MutableList<Status>
    private var pos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_style_twitter, parent, false)
        view.imageTweet.setShape(10F)
        view.profilePhotoTwitter.setShape(30F)
        return AdapterViewHolder(view)
    }

    fun getPosition(): Int = pos

    fun cleanList() {
        listUserTweet = mutableListOf()
    }

    override fun getItemCount(): Int = listUserTweet.size

    interface OnClickImageTweet {
        fun onClickImageTweet(mediaUrl: String?, mediaType: String?)
    }

    interface OnclickTweet {
        fun onclickTweet(tweetUrl: String)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) =
        holder.bind(listUserTweet[position])

    inner class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        lateinit var passMediaUrl: String
        private lateinit var passMediaType: String
        lateinit var passTweetUrl: String
        fun bind(data: Status) {

            with(data.mediaEntity()) {
                passMediaUrl = takeIf { this?.type == "video"}?.getMediaUrl() ?: (this?.mediaURLHttps ?: "")
                passTweetUrl = "https://twitter.com/${data.user.screenName}/status/${data.id}"
                passMediaType = this?.type ?: ""

                if (data.retweetedStatus == null) itemView.userRefreshUiTwitter(data)
                else itemView.retweetUserRefreshUiTwitter(data)

                itemView.loadImageTweet(passMediaType, passMediaUrl)
            }
            itemView.imageTweet.setOnClickListener(this)
            itemView.setOnClickListener { itemView -> goToTweetUrl(passTweetUrl, itemView) }
        }

        override fun onClick(v: View?) {
            listenerImageTweet.onClickImageTweet(passMediaUrl, passMediaType)
        }
    }

    fun goToTweetUrl(passTweetUrl: String, view: View) {
        try {
            startActivity(
                view.context,
                Intent(Intent.ACTION_VIEW, Uri.parse(passTweetUrl)),
                null
            )

        } catch (e: Exception) {
            Log.i("Carpul", "bind: $e")
        }
    }
}