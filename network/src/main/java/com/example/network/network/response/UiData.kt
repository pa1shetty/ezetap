package com.example.network.network.response

import com.google.gson.annotations.SerializedName

data class UiData(
    val hint: String?,
    val key: String?,
    @SerializedName("uitype")
    val uiType: String?,
    var value: String?
)