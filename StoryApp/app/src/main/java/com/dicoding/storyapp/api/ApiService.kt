package com.dicoding.storyapp.api

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val token: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUFmMDNiWkloMi1sbW4ycW8iLCJpYXQiOjE2ODIxNzA5NTV9.0aMjCtcw6B7sNppymxhtxUE84YPuVQT3WPUohgd5ZTI"

interface ApiService {
    @FormUrlEncoded
    @POST("v1/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("v1/register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @Multipart
    @POST("v1/stories")
    fun uploadStory(
        @Header("Authorization") token: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?
    ): Call<StoryUploadResponse>

    @GET("v1/stories")
    suspend fun getStories(
        @Header("Authorization") token: String?,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): StoryResponse

    @GET("v1/stories?location=1&size=99999")
    @Headers("Authorization: Bearer $token")
    fun getStoryWithLocation(): Call<StoryResponse>

    @GET("v1/stories/{id}")
    fun getStoryDetail(
        @Path("id") id: String?,
        @Header("Authorization") token: String?
    ): Call<StoryDetailResponse>

}

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java )
        }
    }
}