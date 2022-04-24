package com.example.network.network.response

import com.example.network.network.response.Status.DEFAULT

data class Response(
    val uiResponse: Any=UiResponse(),
    val status: Status = DEFAULT,
    val errorMessage: String = ErrorMessage.ERROR.message
)

enum class Status {
    SUCCESS,
    FAILURE,
    DEFAULT
}

enum class ErrorMessage(val message: String) {
    NO_INTERNET("No Internet Connection"),
    ERROR("Something Went Wrong!!"),
}