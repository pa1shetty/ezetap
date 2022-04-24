package com.example.network.network

import com.example.network.utils.ApiException
import retrofit2.Response


abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): String {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body().toString()
        } else {
            throw ApiException(response.message())
        }
    }


}