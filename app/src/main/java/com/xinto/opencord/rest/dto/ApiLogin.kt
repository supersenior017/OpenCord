package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiLogin(
    @SerialName("token")
    val token: String? = null,

    @SerialName("mfa")
    val mfa: Boolean = false,

    @SerialName("user_settings")
    val userSettings: ApiLoginUserSettings? = null,

    @SerialName("captcha_sitekey")
    val captchaSiteKey: String? = null,
)

@Serializable
data class ApiLoginUserSettings(
    @SerialName("locale")
    val locale: String,

    @SerialName("theme")
    val theme: String,
)
