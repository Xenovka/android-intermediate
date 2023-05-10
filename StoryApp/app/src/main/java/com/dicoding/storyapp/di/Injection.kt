package com.dicoding.storyapp.di

import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.data.StoryRepository

object Injection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}