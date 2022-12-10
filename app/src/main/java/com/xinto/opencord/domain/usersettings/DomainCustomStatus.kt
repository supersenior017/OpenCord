package com.xinto.opencord.domain.usersettings

import com.xinto.opencord.rest.dto.ApiCustomStatus
import kotlinx.datetime.Instant

data class DomainCustomStatus(
    val text: String?,
    val expiresAt: Instant?,
    val emojiId: Long?,
    val emojiName: String?
)

fun ApiCustomStatus.toDomain(): DomainCustomStatus {
    return DomainCustomStatus(
        text = text,
        expiresAt = expiresAt?.let { Instant.parse(it) },
        emojiId = emojiId?.value,
        emojiName = emojiName,
    )
}
