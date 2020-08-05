package com.example.socialnetwork

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

fun View.hide(){ isVisible = false}  //Using KTX

fun View.show(){ isVisible = true }

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