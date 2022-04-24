package com.example.network.network

import com.example.network.network.response.UiResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import javax.inject.Inject


class NetworkRepository @Inject constructor(
    private val apiService: ApiService
) : SafeApiRequest() {


    companion object {
        var gson: Gson = GsonBuilder()
            .setLenient()
            .create()
    }

    suspend fun getFeeds(): UiResponse {
        return gson.fromJson(apiRequest {
            apiService.getFeeds()
        }, UiResponse::class.java)

    }


    suspend fun fetchImage(url: String) = apiService.fetchImage(url)
}