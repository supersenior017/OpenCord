package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName

data class ApiGuild(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("icon") val icon: String,
)