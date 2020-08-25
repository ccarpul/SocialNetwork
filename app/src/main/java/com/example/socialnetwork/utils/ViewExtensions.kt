package com.example.socialnetwork.utils

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.socialnetwork.R
import kotlinx.android.synthetic.main.recycler_style_twitter.view.*
import twitter4j.Status

fun makeToast(context: Context?, message: String) {

    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
    val layout = toast.view as LinearLayout
    layout.setBackgroundResource(R.drawable.custom_bg_edittext)
    layout.setPadding(20)
    val viewMessage = layout.getChildAt(0) as TextView
    viewMessage.setTextColor(Color.WHITE)
    viewMessage.textSize = 16f
    toast.apply {
        setGravity(Gravity.BOTTOM, 0, 200)
        show()
    }
}

fun View.hide() {
    isVisible = false
}  //Using KTX

fun View.show() {
    isVisible = true
}

fun View.gone(){
    isGone = true
}

fun RecyclerView.isLastArticleDisplayed(linearLayoutManager: LinearLayoutManager): Boolean {

    val totalItems = this.adapter?.itemCount
    if (totalItems != 0) {
        if (RecyclerView.NO_POSITION != linearLayoutManager.findLastCompletelyVisibleItemPosition() &&
            linearLayoutManager.findLastCompletelyVisibleItemPosition() == totalItems?.minus(1)
        )
            return true
    }
    return false
}

fun View.userRefreshUi(data: Status) {
    retweetUser.hide()
    textTweet.text = data.text
    userTwitter.text = data.user.name
    likesCountTweet.text = data.user.favouritesCount.toString()
    screenNameTwitter.text = "@" + data.user.screenName
    retweetCount.text = data.retweetCount.toString()
    if (data.user.isVerified)
        screenNameTwitter.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_verified_tw, 0, 0, 0
        )
    if (!data.user.profileImageURLHttps.isNullOrBlank()) {
        Glide.with(this.context)
            .load(data.user.profileImageURLHttps.getClearImageUrl())
            .override(140, 140)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
            .into(profilePhotoTwitter)
    } else profilePhotoTwitter.setImageDrawable(resources.getDrawable(R.drawable.ic_profile_default))
}

fun View.retweetUserRefreshUi(data: Status) {

    retweetUser.text = data.user.name + " Retwitted"
    retweetUser.show()
    textTweet.text = data.retweetedStatus.text
    userTwitter.text = data.retweetedStatus.user.name
    likesCountTweet.text = data.retweetedStatus.user.favouritesCount.toString()
    screenNameTwitter.text = "@" + data.retweetedStatus.user.screenName
    retweetCount.text = data.retweetedStatus.retweetCount.toString()
    if (data.retweetedStatus.user.isVerified)
        screenNameTwitter.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_verified_tw, 0, 0, 0
        )

    if (!data.retweetedStatus.user.profileImageURLHttps.isNullOrBlank()) {
        Glide.with(this.context)
            .load(data.retweetedStatus.user.profileImageURLHttps.getClearImageUrl())
            .override(140, 140)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
            .into(profilePhotoTwitter)
    } else profilePhotoTwitter.setImageDrawable(resources.getDrawable(R.drawable.ic_profile_default))
}

fun View.loadImageTweet(mediaType: String?, mediaUrl: String?) {

    Glide.with(this.context)
        .apply {
            if (mediaType == "animate_gif")
                this.asGif()
        }
        .load(mediaUrl)
        .centerCrop()
        .into(imageTweet)
    cardImageTweet.show()

    when(mediaType){
        "video" -> iconPlayVideo.show()
        null -> cardImageTweet.hide()
        else -> { iconPlayVideo.hide()
        }
    }
}

