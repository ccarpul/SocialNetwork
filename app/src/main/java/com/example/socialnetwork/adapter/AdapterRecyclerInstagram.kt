package com.example.socialnetwork.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetwork.R
import com.example.socialnetwork.data.model.Data
import com.example.socialnetwork.data.model.ModelResponse
import com.example.socialnetwork.utils.loadImageInstagram
import kotlinx.android.synthetic.main.recycler_style_instagram.view.*


class AdapterRecyclerInstagram(
    private var list: ArrayList<Data>

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val originalList: ArrayList<Data> = arrayListOf()

    private var pos = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_style_instagram, parent, false)
        return AdapterViewHolder(view)
    }

    fun addData(data: ModelResponse) {
        list.addAll(data.data)
        originalList.addAll(list)
        notifyDataSetChanged()
    }

    fun getPosition(): Int {
        return pos
    }

    fun getOriginalList(): MutableList<Data> {
        return originalList
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AdapterViewHolder) {
            pos = position

            holder.bind(list[position])
        }
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