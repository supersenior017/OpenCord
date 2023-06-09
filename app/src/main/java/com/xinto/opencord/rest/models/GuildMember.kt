package com.xinto.opencord.rest.models

import com.xinto.opencord.rest.models.user.ApiUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiGuildMember(
    @SerialName("user")
    val user: ApiUser?,

    @SerialName("nick")
    val nick: String?,

    @SerialName("avatar")
    val avatar: String?
)
