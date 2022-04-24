package com.example.network

import android.content.Context
import android.graphics.BitmapFactory
import com.example.network.network.NetworkRepository
import com.example.network.network.response.ErrorMessage
import com.example.network.network.response.Response
import com.example.network.network.response.Status
import com.example.network.utils.NoInternetException
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

class NetworkCalls @Inject constructor(appContext: Context) {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface InitializerEntryPoint {
        fun getNetworkRepository(): NetworkRepository
    }

    private val networkRepository = EntryPointAccessors.fromApplication(
        appContext,
        InitializerEntryPoint::class.java
    ).getNetworkRepository()

    suspend fun getUi(): Response {
        return try {
            Response(
                networkRepository.getFeeds(), Status.SUCCESS
            )
        } catch (e: NoInternetException) {
            Response(status = Status.FAILURE, errorMessage = ErrorMessage.NO_INTERNET.message)
        } catch (e: Exception) {
            Response(status = Status.FAILURE, errorMessage = ErrorMessage.ERROR.message)
        }
    }

    suspend fun getImage(url: String): Response {
        return try {
            val response = networkRepository.fetchImage(url)
            if (response.isSuccessful) {
                Response(
                    (BitmapFactory.decodeStream(response.body()!!.byteStream())), Status.SUCCESS
                )
            } else {
                Response(status = Status.FAILURE, errorMessage = ErrorMessage.ERROR.message)
            }
        } catch (e: NoInternetException) {
            Response(status = Status.FAILURE, errorMessage = ErrorMessage.NO_INTERNET.message)
        } catch (e: Exception) {
            Response(status = Status.FAILURE, errorMessage = ErrorMessage.ERROR.message)
        }
    }


}