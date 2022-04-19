package com.xinto.opencord.domain.mapper

import com.xinto.opencord.domain.model.*
import com.xinto.opencord.rest.dto.*
import com.xinto.opencord.rest.service.DiscordCdnServiceImpl

fun ApiAttachment.toDomain(): DomainAttachment {
    return if (contentType.isNotEmpty()) {
        when (contentType) {
            "video/mp4" -> DomainAttachment.Video(
                id = id.value,
                filename = filename,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100
            )
            else -> DomainAttachment.Picture(
                id = id.value,
                filename = filename,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100
            )
        }
    } else {
        DomainAttachment.File(
            id = id.value,
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
        )
    }
}

fun ApiChannel.toDomain(): DomainChannel {
    return when (type) {
        2 -> DomainChannel.VoiceChannel(
            id = id.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            permissions = permissions.toDomain()
        )
        4 -> DomainChannel.Category(
            id = id.value,
            name = name,
            position = position,
            permissions = permissions.toDomain()
        )
        5 -> DomainChannel.AnnouncementChannel(
            id = id.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            permissions = permissions.toDomain(),
            nsfw = nsfw
        )
        else -> DomainChannel.TextChannel(
            id = id.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            permissions = permissions.toDomain(),
            nsfw = nsfw
        )
    }
}

fun ApiGuild.toDomain(): DomainGuild {
    val iconUrl = icon?.let { icon ->
        DiscordCdnServiceImpl.getGuildIconUrl(id.toString(), icon)
    }
    val bannerUrl = banner?.let { banner ->
        DiscordCdnServiceImpl.getGuildBannerUrl(id.toString(), banner)
    }
    return DomainGuild(
        id = id.value,
        name = name,
        iconUrl = iconUrl,
        bannerUrl = bannerUrl,
        permissions = permissions.toDomain()
    )
}

fun ApiGuildMember.toDomain(): DomainGuildMember {
    val avatarUrl = user?.let { user ->
        avatar?.let { avatar ->
            DiscordCdnServiceImpl.getUserAvatarUrl(user.id.toString(), avatar)
        } ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(user.discriminator.toInt().rem(5))
    }
    val domainUser = user?.toDomain()
    return DomainGuildMember(
        user = domainUser,
        nick = nick,
        avatarUrl = avatarUrl
    )
}

fun ApiGuildMemberChunk.toDomain(): DomainGuildMemberChunk {
    val domainMembers = members.map { it.toDomain() }
    return DomainGuildMemberChunk(
        guildId = guildId.value,
        guildMembers = domainMembers,
        chunkIndex = chunkIndex,
        chunkCount = chunkCount,
    )
}

fun ApiMeGuild.toDomain(): DomainMeGuild {
    val iconUrl = icon?.let { icon ->
        DiscordCdnServiceImpl.getGuildIconUrl(id.toString(), icon)
    }
    return DomainMeGuild(
        id = id.value,
        name = name,
        iconUrl = iconUrl,
        permissions = permissions.toDomain()
    )
}

fun ApiMessage.toDomain(): DomainMessage {
    val domainAuthor = author.toDomain()
    val domainAttachments = attachments.map { it.toDomain() }
    return DomainMessage(
        id = id.value,
        content = content,
        channelId = channelId.value,
        author = domainAuthor,
        timestamp = timestamp,
        attachments = domainAttachments,
        embeds = listOf()
    )
}

fun ApiUser.toDomain(): DomainUser {
    val avatarUrl = avatar?.let { avatar ->
        DiscordCdnServiceImpl.getUserAvatarUrl(id.toString(), avatar)
    } ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(discriminator.toInt().rem(5))
    return DomainUser(
        id = id.value,
        username = username,
        discriminator = discriminator,
        avatarUrl = avatarUrl,
        bot = bot,
    )
}

fun ApiPermissions.toDomain(): List<DomainPermission> {
    val permissions = value
    return DomainPermission.values().filter {
        (permissions and it.flags) == it.flags
    }
}