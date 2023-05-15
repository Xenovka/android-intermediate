package com.dicoding.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.storyapp.api.ApiService
import com.dicoding.storyapp.api.StoryItem

class StoryRepository(private val apiService: ApiService) {
    fun getStories(token: String): LiveData<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }
}