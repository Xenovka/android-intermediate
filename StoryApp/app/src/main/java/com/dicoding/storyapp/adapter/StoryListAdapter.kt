package com.dicoding.storyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.api.StoryItem

class StoryListAdapter : RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {
    interface OnItemClickCallback {
        fun onItemClicked(data: StoryItem)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val stories = ArrayList<StoryItem>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItemName: TextView = view.findViewById(R.id.tv_item_name)
        val ivItemPhoto: ImageView = view.findViewById(R.id.iv_item_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = stories[position]

        holder.apply {
            Glide.with(itemView)
                .load(story.photoUrl)
                .centerCrop()
                .into(ivItemPhoto)

            tvItemName.text = story.name

            itemView.setOnClickListener { onItemClickCallback.onItemClicked(stories[adapterPosition]) }
        }
    }

    override fun getItemCount(): Int = stories.size

    fun setStories(list: ArrayList<StoryItem>) {
        stories.clear()
        stories.addAll(list)
        notifyDataSetChanged()
    }
}