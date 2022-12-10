package com.xinto.opencord.domain.login

import com.xinto.opencord.rest.dto.ApiLogin

sealed interface DomainLogin {
    data class Login(
        val token: String,
        val mfa: Boolean,
        val locale: String,
        val theme: String,
    ) : DomainLogin

    data class Captcha(val captchaSiteKey: String) : DomainLogin

    data class TwoFactorAuth(val ticket: String) : DomainLogin

    object Error : DomainLogin
}

fun ApiLogin.toDomain(): DomainLogin {
    return when {
        ticket != null -> {
            DomainLogin.TwoFactorAuth(
                ticket = ticket,
            )
        }
        captchaSiteKey != null -> {
            DomainLogin.Captcha(
                captchaSiteKey = captchaSiteKey,
            )
        }
        token != null -> {
            DomainLogin.Login(
                token = token,
                mfa = mfa,
                theme = userSettings?.theme ?: "",
                locale = userSettings?.locale ?: "",
            )
        }
        else -> DomainLogin.Error
    }
}
