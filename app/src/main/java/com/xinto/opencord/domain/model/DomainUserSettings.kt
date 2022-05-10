package com.xinto.opencord.domain.model

import com.xinto.enumgetter.GetterGen
import com.xinto.partialgen.Partial
import kotlinx.datetime.Instant

@Partial
data class DomainUserSettings(
    val locale: String,
    val showCurrentGame: Boolean,
//    val restrictedGuilds: List<ULong>
//    val defaultGuildsRestricted: Boolean,
    val inlineAttachmentMedia: Boolean,
    val inlineEmbedMedia: Boolean,
    val gifAutoPlay: Boolean,
    val renderEmbeds: Boolean,
    val renderReactions: Boolean,
    val animateEmoji: Boolean,
    val enableTTSCommand: Boolean,
    val messageDisplayCompact: Boolean,
    val convertEmoticons: Boolean,
//    val explicitContentFilter: Int,
    val disableGamesTab: Boolean,
    val theme: DomainThemeSetting,
    val developerMode: Boolean,
    val guildPositions: List<ULong>,
    val detectPlatformAccounts: Boolean,
    val status: DomainUserStatus,
    val afkTimeout: Int,
    val timezoneOffset: Int,
    val streamNotificationsEnabled: Boolean,
    val allowAccessibilityDetection: Boolean,
    val contactSyncEnabled: Boolean,
    val nativePhoneIntegrationEnabled: Boolean,
    val animateStickers: Int, // int??
    val friendDiscoveryFlags: Int, // int??
    val viewNsfwGuilds: Boolean,
    val passwordless: Boolean,
    val friendSourceFlags: DomainFriendSources,
    val guildFolders: List<DomainGuildFolder>,
    val customStatus: DomainCustomStatus?,
)

data class DomainFriendSources(
    val all: Boolean,
    val mutualFriends: Boolean,
    val mutualGuilds: Boolean,
)

data class DomainGuildFolder(
    // Null id means all the guildIds are outside of a folder
    val id: ULong? = null,
    val guildIds: List<ULong>,
    val name: String? = null,
//    val color: Any? = null,
)

data class DomainCustomStatus(
    val text: String,
    val expiresAt: Instant?,
    val emojiId: ULong?,
    val emojiName: String?
)

@GetterGen
enum class DomainThemeSetting(val value: String) {
    Dark("dark"),
    Light("light");

    companion object
}

@GetterGen
enum class DomainUserStatus(val value: String) {
    Online("online"),
    Idle("idle"),
    Dnd("dnd"),
    Invisible("invisible");

    companion object
}
