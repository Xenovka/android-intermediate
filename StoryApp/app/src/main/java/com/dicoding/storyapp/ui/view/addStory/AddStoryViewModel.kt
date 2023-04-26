package com.dicoding.storyapp.ui.view.addStory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.api.StoryUploadResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryViewModel: ViewModel() {
    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    fun uploadStory(file: File?, description: String) {
        if(file != null) {
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            ApiConfig.getApiService()
                .uploadStory(imageMultiPart, description)
                .enqueue(object: Callback<StoryUploadResponse> {
                    override fun onResponse(
                        call: Call<StoryUploadResponse>,
                        response: Response<StoryUploadResponse>
                    ) {
                        if(response.isSuccessful) {
                            val result = response.body()
                            if(result != null && !result.error) {
                                _isSuccessful.value = true
                            }
                        } else {
                            _isSuccessful.value = false
                        }
                    }

                    override fun onFailure(call: Call<StoryUploadResponse>, t: Throwable) {
                        Log.e("UploadError", "onFailure: ${t.message}")
                    }
                })
        }

    }
}