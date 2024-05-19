package com.dicoding.picodiploma.loginwithanimation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem

class StoryAdapter(private var listStories: List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(storyItem: ListStoryItem) {
            itemView.findViewById<TextView>(R.id.tvName).text = storyItem.name
            itemView.findViewById<TextView>(R.id.tvDescription).text = storyItem.description
            Glide.with(itemView.context)
                .load(storyItem.photoUrl)
                .into(itemView.findViewById<ImageView>(R.id.ivPhoto))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStories[position])
    }

    override fun getItemCount(): Int = listStories.size

    fun submitList(newList: List<ListStoryItem>) {
        listStories = newList
        notifyDataSetChanged()
    }
}