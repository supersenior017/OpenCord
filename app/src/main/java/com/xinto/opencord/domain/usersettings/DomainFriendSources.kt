package com.xinto.opencord.domain.usersettings

import com.xinto.opencord.rest.dto.ApiFriendSources

data class DomainFriendSources(
    val all: Boolean,
    val mutualFriends: Boolean,
    val mutualGuilds: Boolean,
)

fun ApiFriendSources.toDomain(): DomainFriendSources {
    return DomainFriendSources(
        all = (all ?: false) && mutualFriends == null && mutualGuilds == null,
        mutualFriends = mutualFriends ?: false,
        mutualGuilds = mutualGuilds ?: false,
    )
}
