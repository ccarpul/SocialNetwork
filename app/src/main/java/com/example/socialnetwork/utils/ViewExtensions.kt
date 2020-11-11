package com.example.socialnetwork.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.socialnetwork.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.CornerSize
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.android.synthetic.main.profile_style.view.*
import kotlinx.android.synthetic.main.recycler_style_instagram.view.*
import kotlinx.android.synthetic.main.recycler_style_twitter.view.*
import twitter4j.MediaEntity
import twitter4j.Status

fun makeToast(context: Context?, message: String) {

    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        .apply {
            setGravity(Gravity.BOTTOM, 0, 400)
            show()
        }
    (toast.view as LinearLayout).apply {
        setBackgroundResource(R.drawable.custom_bg_edittext)
        setPadding(20)
        (getChildAt(0) as TextView).apply {
            setTextColor(Color.WHITE)
            textSize = 16f
        }
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
    return if (totalItems != 0)
        RecyclerView.NO_POSITION != linearLayoutManager.findLastCompletelyVisibleItemPosition() &&
                linearLayoutManager.findLastCompletelyVisibleItemPosition() == totalItems?.minus(1)
    else false
}

fun View.refreshUi(data: Status?) {

    descriptionImageInstagram.text = data?.text

    data?.user?.let {
        userTwitter.text = it.name
        likesCountTweet.text = it.favouritesCount.toString()
        screenNameTwitter.text = "@ + ${it?.screenName}"
        retweetCount.text = data?.retweetCount.toString()
        screenNameTwitter.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_verified_tw, 0, 0, 0
        )

        if (!it.profileImageURLHttps.isNullOrBlank()) {
            Glide.with(context)
                .load(it.profileImageURLHttps?.getClearImageUrl())
                .override(140, 140)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                .into(profilePhotoTwitter)
        } else profilePhotoTwitter
            .setImageDrawable(resources.getDrawable(R.drawable.ic_profile_default))

    }
}

fun View.loadImageTweet(mediaType: String, mediaUrl: String) {

    Glide.with(context)
        .load(mediaUrl)
        .into(imageTweet)
    imageTweet.show()

    if (mediaType == "video")
        iconPlayVideo.show()
    else if (mediaUrl.isEmpty()) {
        imageTweet.hide()
        iconPlayVideo.visibility = View.INVISIBLE
    }
}


fun View.loadImageInstagram(mediaUrl: String) {

    if (!mediaUrl.isBlank()) {

        Glide.with(context)
            .load(mediaUrl)
            .into(imageInstagram)

        Glide.with(context)
            .load(mediaUrl)
            .override(80, 80)
            .placeholder(R.drawable.ic_profile_default)
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
    visible?.let { visibility = it }
    userNameToolbar.visibility = userNameVisible
    screenNameToolbar.visibility = screenVisible
    imageProviderToolbar.visibility = imageProviderVisible
    textScreenName?.let { userNameToolbar.text = textUserName }
    textScreenName?.let {
        if (it.isNotBlank()) screenNameToolbar.text = it else screenNameToolbar.gone()
    }

    imageProvider?.let { imageProviderToolbar.setImageDrawable(getDrawable(context, it)) }

    if (imageNavigationIcon != null)
        Glide.with(context)
            .asDrawable()
            .load(imageNavigationIcon.getClearImageUrl())
            .override((50 * resources.displayMetrics.density).toInt())
            .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    @NonNull resource: Drawable,
                    @Nullable transition: Transition<in Drawable>?
                ) { navigationIcon = resource }

                override fun onLoadCleared(placeholder: Drawable?) {}
            }
            )
    else navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_hamburger_24)
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
            Glide.with(context)
                .load(imageHeaderUri)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(200)))
                .placeholder(R.mipmap.ic_launcher_foreground)
                .into(imageHeadNavigation)
        }
        imageHeaderUrl != null -> {
            Glide.with(context)
                .load(imageHeaderUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(200)))
                .placeholder(R.mipmap.ic_launcher_foreground)
                .into(imageHeadNavigation)
        }
        else -> imageHeadNavigation.gone()
    }

    if (textHeader != null) textHeaderTitle.text = textHeader
    else textHeaderTitle.gone()

}

fun NavigationView.setupMenuItem(
    id: Int,
    titleMenu: String,
    isSelected: Boolean = false
) {
    menu.findItem(id).apply {
        isEnabled = !isSelected
        title = if (!isEnabled) {
            SpannableString(titleMenu).apply {
                setSpan(ForegroundColorSpan(context.getColor(R.color.colorAccent)),
                    0, titleMenu.length, 0)
            }
        } else titleMenu
    }
}

fun ShapeableImageView.setShape(cornerSize: Float = 20F) {
    shapeAppearanceModel = shapeAppearanceModel
        .toBuilder()
        .setAllCorners(CornerFamily.ROUNDED, cornerSize)
        .build()
}

