package com.dicoding.storyapp.ui.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.api.StoryItem
import com.dicoding.storyapp.api.StoryResponse
import com.dicoding.storyapp.model.UserModel
import com.dicoding.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference) : ViewModel() {
    val stories = MutableLiveData<ArrayList<StoryItem>>()

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun setAllStories(token: String?) {
        ApiConfig.getApiService()
            .getAllStories("Bearer $token")
            .enqueue(object: Callback<StoryResponse> {
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    Log.d("AuthToken", "Bearer $token")
                    if(response.isSuccessful) {
                        stories.postValue(response.body()?.listStory)
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    Log.e("ErrorFetchStories", "onFailure: ${t.message}")
                }
            })
    }

    fun getAllStories(): LiveData<ArrayList<StoryItem>> {
        return stories
    }
}