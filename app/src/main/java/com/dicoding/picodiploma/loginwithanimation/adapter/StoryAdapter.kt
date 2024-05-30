package com.dicoding.picodiploma.loginwithanimation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailActivity

class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(storyItem: ListStoryItem) {
            itemView.findViewById<TextView>(R.id.tvName).text = storyItem.name
            itemView.findViewById<TextView>(R.id.tvDescription).text = storyItem.description
            Glide.with(itemView.context).load(storyItem.photoUrl).fitCenter()
                .into(itemView.findViewById<ImageView>(R.id.ivPhoto))

            itemView.setOnClickListener { view ->
                val intentDetail = Intent(view.context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_DETAIL, storyItem)
                }
                view.context.startActivity(intentDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listStory = getItem(position)
        if (listStory != null) {
            holder.bind(listStory)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
