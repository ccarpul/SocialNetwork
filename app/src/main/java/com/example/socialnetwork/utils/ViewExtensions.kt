package com.example.socialnetwork.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.socialnetwork.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.android.synthetic.main.profile_style.view.*
import kotlinx.android.synthetic.main.recycler_style_instagram.view.*
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
        setGravity(Gravity.BOTTOM, 0, 400)
        show()
    }
}

fun View.hide() {
    isVisible = false
}  //Using KTX

fun View.show() {
    isVisible = true
}

fun View.gone() {
    isGone = true
}

fun RecyclerView.isLastArticleDisplayed(linearLayoutManager: LinearLayoutManager): Boolean {

    val totalItems = adapter?.itemCount
    return if(totalItems != 0)
        RecyclerView.NO_POSITION != linearLayoutManager.findLastCompletelyVisibleItemPosition() &&
            linearLayoutManager.findLastCompletelyVisibleItemPosition() == totalItems?.minus(1)
    else false
}

fun View.userRefreshUiTwitter(data: Status) {
    retweetUser.hide()
    descriptionImageInstagram.text = data.text
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

fun View.retweetUserRefreshUiTwitter(data: Status) {

    retweetUser.text = data.user.name + context.getString(R.string.retwitted)
    retweetUser.show()
    descriptionImageInstagram.text = data.retweetedStatus.text
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

    if (!mediaUrl.isNullOrBlank()) {
        Glide.with(this.context)
            .apply {
                if (mediaType == "animate_gif")
                    this.asGif()
            }
            .load(mediaUrl)
            .centerCrop()
            .into(imageTweet)
        cardImageTweet.show()
    }
    when (mediaType) {
        "video" -> iconPlayVideo.show()
        null -> cardImageTweet.hide()
        else -> iconPlayVideo.hide()
    }
}

fun View.loadImageInstagram(mediaUrl: String) {

    /*
    if (mediaUrl.contains("video")) {
        //imageInstagram.gone()
        //videoViewIg.setVideoPath(mediaUrl)
        //videoViewIg.start()
        //videoViewIg.show()
        //videoViewIg.pause()
        imageInstagram.hide()
    } else {*/
    //videoViewIg.gone()

    if (!mediaUrl.isNullOrBlank()) {

        Glide.with(this.context)
            .load(mediaUrl)
            .into(imageInstagram)

        Glide.with(this.context)
            .load(mediaUrl)
            .placeholder(R.drawable.ic_profile_default)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(200)))
            .into(profilePhotoInstagram)
    }
}

fun Toolbar.setupToolbar(
    visible: Int? = View.VISIBLE,
    userNameVisible: Int = View.VISIBLE,
    screenVisible: Int = View.VISIBLE,
    imageProviderVisible: Int = View.VISIBLE,
    textUserName: String? = null,
    textScreenName: String? = null,
    imageProvider: Int? = null,
    imageNavigationIcon: String? = null
) {

    if (visible != null) this.visibility = visible

    screenNameToolbar.visibility = screenVisible
    userNameToolbar.visibility = userNameVisible
    imageProviderToolbar.visibility = imageProviderVisible

    if (textUserName != null) userNameToolbar.text = textUserName

    if (textScreenName != null || textScreenName != "") screenNameToolbar.text = textScreenName
    else screenNameToolbar.gone()

    if (imageProvider != null)
        imageProviderToolbar.setImageDrawable(getDrawable(this.context, imageProvider))

    if (imageNavigationIcon == null)
        navigationIcon = ContextCompat.getDrawable(this.context, R.drawable.ic_hamburger_24)
    else {
        Glide.with(this.context).asBitmap()
            .load(imageNavigationIcon.getClearImageUrl())
            .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
            .into(object : SimpleTarget<Bitmap>(140, 140) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    navigationIcon = BitmapDrawable(resources, resource)
                }
            })
    }
}

fun ViewGroup.setupHeaderNav(
    textHeader: String? = null,
    imageHeaderDrawable: Int? = null,
    imageHeaderUrl: String? = null,
    imageHeaderUri: String? = null
) {

    when {
        imageHeaderDrawable != null -> imageHeadNavigation.setImageResource(imageHeaderDrawable)
        imageHeaderUri != null -> {
            Glide.with(this.context)
                .load(imageHeaderUri)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(200)))
                .placeholder(R.mipmap.ic_launcher_foreground)
                .into(imageHeadNavigation)
        }
        imageHeaderUrl != null -> {
            Glide.with(this.context)
                .load(imageHeaderUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(200)))
                .placeholder(R.mipmap.ic_launcher_foreground)
                .into(imageHeadNavigation)
        }
        else -> imageHeadNavigation.gone()
    }

    if (textHeader != null) textHeaderTitle.text = textHeader
    else this.textHeaderTitle.gone()

}

fun NavigationView.setupMenuItem(
    id: Int,
    titleMenu: String,
    isSelected: Boolean = false
) {

    val color = this.context.getColor(R.color.colorAccent)
    menu.findItem(id).apply {
        isEnabled = !isSelected
        title = if (isSelected) {
            val titleMenuSpannable = SpannableString(titleMenu)
            titleMenuSpannable.setSpan(ForegroundColorSpan(color), 0, titleMenu.length, 0)
            titleMenuSpannable
        } else titleMenu
    }
}

