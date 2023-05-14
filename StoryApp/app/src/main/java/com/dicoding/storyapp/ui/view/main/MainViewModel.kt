package com.dicoding.storyapp.ui.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.api.StoryItem
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.model.UserModel
import com.dicoding.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference, private val storyRepository: StoryRepository) : ViewModel() {
    fun story(token: String): LiveData<PagingData<StoryItem>> = storyRepository.getStories(token).cachedIn(viewModelScope)

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}