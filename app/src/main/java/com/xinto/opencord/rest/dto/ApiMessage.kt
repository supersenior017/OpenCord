package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiMessage(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("channel_id")
    val channelId: ApiSnowflake,

    @SerialName("content")
    val content: String,

    @SerialName("author")
    val author: ApiUser,

    @SerialName("attachments")
    val attachments: List<ApiAttachment>,
)