package com.dicoding.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.api.ApiService
import com.dicoding.storyapp.api.StoryItem

class StoryRepository(private val apiService: ApiService) {
    fun getStories(): LiveData<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }
}