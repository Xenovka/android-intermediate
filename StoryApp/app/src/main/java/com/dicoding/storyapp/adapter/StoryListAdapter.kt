package com.dicoding.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.api.StoryItem
import com.dicoding.storyapp.databinding.StoryItemBinding

class StoryListAdapter :
    PagingDataAdapter<StoryItem, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {
    interface OnItemClickCallback {
        fun onItemClicked(data: StoryItem)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null) {
            holder.bind(data)
            holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(data) }
        }
    }

    class ViewHolder(private val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryItem) {
            Glide.with(itemView)
                .load(data.photoUrl)
                .centerCrop()
                .into(binding.ivItemPhoto)

            binding.tvItemName.text = data.name
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}