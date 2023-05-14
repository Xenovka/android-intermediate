package com.dicoding.storyapp.ui.view.addStory

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.api.StoryUploadResponse
import com.dicoding.storyapp.model.UserModel
import com.dicoding.storyapp.model.UserPreference
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryViewModel(private val pref: UserPreference): ViewModel() {
    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun uploadStory(file: File?, description: String, token: String?, lat: Float?, lon: Float?) {
        if(file != null && token != null) {
            val imageDescription = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            ApiConfig.getApiService()
                .uploadStory("Bearer $token", imageMultiPart, imageDescription, lat, lon)
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