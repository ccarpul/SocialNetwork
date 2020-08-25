package com.example.socialnetwork.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.utils.*
import kotlinx.android.synthetic.main.recycler_style_twitter.view.*
import twitter4j.MediaEntity
import twitter4j.Status


class AdapterRecyclerTwitter(

    private var listUserTweet: MutableList<Status>,
    private val listener: OnClickImageTweet

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
        fun onClick(mediaUrl: String?, mediaType: String?)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AdapterViewHolder) {
            pos = position

            val mediaEntity = mediaEntity(listUserTweet[position])
            val mediaVideo =
                if (mediaEntity?.type == "video") mediaEntity?.getMediaUrl() else null

            holder.bind(
                listUserTweet[position], mediaEntity?.mediaURLHttps, mediaEntity?.type,
                mediaVideo
            )
        }
    }

    inner class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var passMediaUrl: String? = ""
        var passMediaType: String? = ""
        fun bind(data: Status, mediaUrl: String?, mediaType: String?, mediaUrlVideo: String?) {

            passMediaUrl = mediaUrlVideo ?: mediaUrl
            passMediaType = mediaType
            itemView.imageTweet.setOnClickListener(this)
            if (data.retweetedStatus == null) {
                itemView.userRefreshUi(data)
            } else itemView.retweetUserRefreshUi(data)

            itemView.loadImageTweet(mediaType, mediaUrl)

        }
        override fun onClick(v: View?) { listener.onClick(passMediaUrl, passMediaType) }
    }
}