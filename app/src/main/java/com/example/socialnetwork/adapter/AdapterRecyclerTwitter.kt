package com.example.socialnetwork.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.utils.*
import kotlinx.android.synthetic.main.recycler_style_twitter.view.*
import twitter4j.Status


class AdapterRecyclerTwitter(

    private var listUserTweet: MutableList<Status>,
    private val listenerImageTweet: OnClickImageTweet

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var originalList: MutableList<Status>
    private var pos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_style_twitter, parent, false)
        return AdapterViewHolder(view)
    }

    fun addData(data: List<Status>) {
        listUserTweet.addAll(data)
        originalList = listUserTweet
        notifyDataSetChanged()
    }

    fun getPosition(): Int {
        return pos
    }

    fun cleanList() {
        listUserTweet = mutableListOf()
    }

    fun getOriginalList(): MutableList<Status> {
        return originalList
    }

    override fun getItemCount(): Int = listUserTweet.size

    interface OnClickImageTweet {
        fun onClickImageTweet(mediaUrl: String?, mediaType: String?)
    }

    interface OnclickTweet {
        fun onclickTweet(tweetUrl: String)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is AdapterViewHolder) {

            pos = position

            val mediaEntity = mediaEntity(listUserTweet[position])
            val mediaVideo =
                if (mediaEntity?.type == "video") mediaEntity.getMediaUrl() else null

            holder.bind(
                listUserTweet[position], mediaEntity?.mediaURLHttps, mediaEntity?.type,
                mediaVideo
            )
        }
    }

    inner class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var passMediaUrl:  String? = ""
        var passMediaType: String? = ""
        var passTweetUrl:  String  = ""

        fun bind(data: Status, mediaUrl: String?, mediaType: String?, mediaUrlVideo: String?) {

            passMediaUrl  = mediaUrlVideo ?: mediaUrl
            passMediaType = mediaType
            passTweetUrl  = "https://twitter.com/${data.user.screenName}/status/${data.id}"

            if (data.retweetedStatus == null) itemView.userRefreshUiTwitter(data)
            else itemView.retweetUserRefreshUiTwitter(data)

            itemView.loadImageTweet(mediaType, mediaUrl)
            itemView.imageTweet.setOnClickListener(this)
            itemView.setOnClickListener { goToTweetUrl(passTweetUrl, it) }
        }

        override fun onClick(v: View?) {
            listenerImageTweet.onClickImageTweet(passMediaUrl, passMediaType)
        }
    }

    fun goToTweetUrl(passTweetUrl: String, view: View) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(passTweetUrl))
            startActivity(view.context, intent, null)

        } catch (e: Exception) { Log.i("Carpul", "bind: $e") }
    }
}