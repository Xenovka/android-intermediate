package com.dicoding.storyapp.ui.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.api.StoryItem
import com.dicoding.storyapp.api.StoryResponse
import com.dicoding.storyapp.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val pref: UserPreference): ViewModel() {
    val stories = MutableLiveData<ArrayList<StoryItem>>()

    fun setStories() {
        ApiConfig.getApiService().getStoryWithLocation()
            .enqueue(object: Callback<StoryResponse> {
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    if(response.isSuccessful) {
                        stories.postValue(response.body()?.listStory)
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    Log.d("onFailure: ", t.message.toString())
                }
            })
    }

    fun getStories() : LiveData<ArrayList<StoryItem>> {
        return stories
    }
}