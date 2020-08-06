package com.example.socialnetwork.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.data.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_style.view.description
import kotlinx.android.synthetic.main.recycler_style.view.urlToImage
import kotlinx.android.synthetic.main.recycler_style_twitter.view.*


class AdapterRecyclerTwitter(

    private var listUserTweet: ArrayList<ModelTwitter.ModelTwitterItem>

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val originalList: ArrayList<ModelTwitter.ModelTwitterItem> = arrayListOf()
    private var pos = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_style_twitter, parent, false)
        return AdapterViewHolder(view)
    }

    fun addData(data: ModelTwitter) {
        listUserTweet.addAll(data)
        originalList.addAll(listUserTweet)
        notifyDataSetChanged()
    }

    fun getPosition(): Int {
        return pos
    }

    fun getOriginalList(): ArrayList<ModelTwitter.ModelTwitterItem> {
        return originalList
    }

    override fun getItemCount(): Int = listUserTweet.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AdapterRecyclerTwitter.AdapterViewHolder) {
            pos = position
            holder.bind(listUserTweet[position])
        }
    }

    inner class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ModelTwitter.ModelTwitterItem) {

            itemView.apply {
                description.text = data.text
                user.text = data.user.name
                screenName.text = "@${data.user.screen_name}"
                likesCount.text = data.user.favourites_count.toString()
                retweetCount.text = data.retwit_count.toString()
                if(data.user.verified) screenName
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verificated_24,
                    0,0,0)

                if (!data.user.profile_image_url_https.isNullOrBlank()) {
                    Picasso.with(itemView.context).load(data.user.profile_image_url_https)
                        .placeholder(R.mipmap.ic_launcher_foreground)
                        .resize(24, 24)
                        .centerCrop()
                        .into(urlToImage)
                }else urlToImage.setImageDrawable(resources.getDrawable(R.mipmap.ic_launcher))
            }

        }
    }
}
