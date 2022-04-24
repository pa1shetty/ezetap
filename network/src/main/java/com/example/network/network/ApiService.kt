package com.example.network.network

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url


interface ApiService {


    @GET("android_assignment.json")
    suspend fun getFeeds(
    ): Response<JsonObject>


    @GET
    suspend fun fetchImage(@Url url:String): Response<ResponseBody>
}