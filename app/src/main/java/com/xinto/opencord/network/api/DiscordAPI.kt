package com.xinto.opencord.network.api

import com.xinto.opencord.network.response.ApiChannel
import com.xinto.opencord.network.response.ApiGuild
import com.xinto.opencord.network.response.ApiMeGuild
import com.xinto.opencord.network.response.ApiMessage
import retrofit2.http.GET
import retrofit2.http.Path

interface DiscordAPI {

    @GET("users/@me/guilds")
    suspend fun getMeGuilds(): List<ApiMeGuild>

    @GET("guilds/{guildId}")
    suspend fun getGuild(@Path("guildId") guildId: Long): ApiGuild

    @GET("guilds/{guildId}/channels")
    suspend fun getGuildChannels(@Path("guildId") guildId: Long): List<ApiChannel>

    @GET("channels/{channelId}/messages")
    suspend fun getChannelMessages(@Path("channelId") channelId: Long): List<ApiMessage>
}