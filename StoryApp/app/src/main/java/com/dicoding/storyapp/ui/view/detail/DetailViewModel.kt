package com.dicoding.storyapp.ui.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.api.StoryDetailResponse
import com.dicoding.storyapp.api.StoryItem
import com.dicoding.storyapp.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    val story = MutableLiveData<StoryItem>()

    fun setStoryDetail(userId: String?) {
        ApiConfig.getApiService()
            .getStoryDetail(userId)
            .enqueue(object: Callback<StoryDetailResponse> {
                override fun onResponse(
                    call: Call<StoryDetailResponse>,
                    response: Response<StoryDetailResponse>
                ) {
                    if(response.isSuccessful) {
                        story.postValue(response.body()?.story)
                    }
                }

                override fun onFailure(call: Call<StoryDetailResponse>, t: Throwable) {
                    Log.e("StoryDetail Error", "onFailure: ${t.message}")
                }
            })
    }

    fun getStoryDetail(): LiveData<StoryItem> {
        return story
    }
}