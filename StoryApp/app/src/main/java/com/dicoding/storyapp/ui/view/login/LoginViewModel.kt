package com.dicoding.storyapp.ui.view.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.api.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {
    fun login(email: String, password: String) {
        ApiConfig.getApiService()
            .login(email, password)
            .enqueue(object: Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful) {
                        val responseBody = response.body()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("ErrorLogin", "onFailure: ${t.message}")
                }
            })
    }
}