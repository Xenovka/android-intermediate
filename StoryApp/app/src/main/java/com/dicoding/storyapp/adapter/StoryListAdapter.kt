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
import com.dicoding.storyapp.databinding.StoryItemBinding

class StoryListAdapter : RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {
    interface OnItemClickCallback {
        fun onItemClicked(data: StoryItem)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val stories = ArrayList<StoryItem>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvItemName: TextView = binding.tvItemName
        val ivItemPhoto: ImageView = binding.ivItemPhoto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

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