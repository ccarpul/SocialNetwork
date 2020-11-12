package com.example.socialnetwork.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.data.model.Data
import com.example.socialnetwork.utils.loadImageInstagram
import com.example.socialnetwork.utils.setShape
import kotlinx.android.synthetic.main.recycler_style_instagram.view.*


class AdapterRecyclerInstagram(
    private var list: MutableList<Data>

) : RecyclerView.Adapter<AdapterRecyclerInstagram.AdapterViewHolder>() {

    private var pos = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_style_instagram, parent, false)
        view.profilePhotoInstagram.setShape(85F)
        return AdapterViewHolder(view)
    }

    fun addData(data: Data) {
        list.add(0,data)
        notifyItemInserted(0)
    }

    fun getPosition(): Int = pos

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
            pos = position
            holder.bind(list[position])
    }

    inner class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Data) {
            itemView.apply {
                userInstagram.text = data.username
                textImageInstagram.text = "${data.username} ${data.caption}"
                loadImageInstagram(data.media_url)
            }
        }
    }
}