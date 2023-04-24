package com.dicoding.storyapp.ui.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.api.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    fun register(name: String, email: String, password: String) {
        ApiConfig.getApiService()
            .register(name, email, password)
            .enqueue(object: Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _isSuccessful.value = response.isSuccessful
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.e("ErrorLogin", "onFailure: ${t.message}")
                }
            })
    }
}