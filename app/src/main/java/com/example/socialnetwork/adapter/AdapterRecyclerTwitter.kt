package com.example.socialnetwork.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.socialnetwork.R
import com.example.socialnetwork.utils.*
import kotlinx.android.synthetic.main.recycler_style_twitter.view.*
import twitter4j.Status


class AdapterRecyclerTwitter(

    private var listUserTweet: MutableList<Status>

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

    fun cleanList(){
        listUserTweet = mutableListOf()
    }

    fun getOriginalList(): MutableList<Status> {
        return originalList
    }

    override fun getItemCount(): Int = listUserTweet.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AdapterViewHolder) {
            pos = position

            val mediaEntity = mediaEntity(listUserTweet[position])

            holder.bind(
                listUserTweet[position], mediaEntity?.mediaURLHttps, mediaEntity?.type
            )
        }
    }

    inner class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Status, mediaUrl: String?, mediaType: String?) {

            itemView.apply {

                if (data.retweetedStatus != null) {
                    retweetUser.apply {
                        show()
                        text = data.user.name
                    }

                    description.text = data.retweetedStatus.text
                    user.text = data.retweetedStatus.user.name
                    likesCount.text = data.retweetedStatus.favoriteCount.toString()
                    screenName.text = " @${data.retweetedStatus.user.screenName}"
                    retweetCount.text = data.retweetedStatus.retweetCount.toString()

                    if (!data.retweetedStatus.user.profileImageURLHttps.isNullOrBlank()) {

                        Glide.with(itemView.context)
                            .load(data.retweetedStatus.user.profileImageURLHttps.getClearImageUrl())
                            .override(140, 140)
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                            .into(urlToImage)
                    } else urlToImage.setImageDrawable(resources.getDrawable(R.drawable.ic_profile_default))

                    if (data.retweetedStatus.user.isVerified)
                        screenName.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_verified_tw, 0, 0, 0
                        )

                } else {
                    retweetUser.hide()
                    description.text = data.text
                    retweetCount.text = data.retweetCount.toString()
                    user.text = data.user.name
                    likesCount.text = data.favoriteCount.toString()
                    screenName.text = " @" + data.user.screenName
                    retweetCount.text = data.retweetCount.toString()

                    if (!data.user.profileImageURLHttps.isNullOrBlank()) {

                        Glide.with(itemView.context)
                            .load( data.user.profileImageURLHttps.getClearImageUrl())
                            .override(140, 140)
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                            .into(urlToImage)
                    } else urlToImage.setImageDrawable(resources.getDrawable(R.drawable.ic_profile_default))

                    if (data.user.isVerified)
                        screenName.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_verified_tw, 0, 0, 0
                        )
                }

                if (mediaUrl == null) cardImageTweet.hide()
                else {
                    cardImageTweet.show()
                    Glide.with(itemView.context)
                        .apply {
                            if (mediaType == "animate_gif")
                                this.asGif()
                        }
                        .load(mediaUrl)
                        .centerCrop()
                        .into(imageTweet)
                }

            }
        }
    }
}