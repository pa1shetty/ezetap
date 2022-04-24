package com.example.network.network.response

import com.google.gson.annotations.SerializedName

data class UiResponse(
    @SerializedName("heading-text")
    val heading_text: String?=null,
    @SerializedName("logo-url")
    val logo_url: String?=null,
    @SerializedName("uidata")
    val uiData: List<UiData>?=null
)