package com.dicoding.storyapp.ui.view.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.api.LoginResponse
import com.dicoding.storyapp.model.UserModel
import com.dicoding.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference): ViewModel() {
    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    fun login(email: String, password: String) {
        ApiConfig.getApiService()
            .login(email, password)
            .enqueue(object: Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful) {
                        val result = response.body()?.loginResult
                        viewModelScope.launch {
                            saveUser(UserModel(result?.userId, result?.name, result?.token, true))
                        }
                    }
                    _isSuccessful.value = response.isSuccessful
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("ErrorLogin", "onFailure: ${t.message}")
                }
            })
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}