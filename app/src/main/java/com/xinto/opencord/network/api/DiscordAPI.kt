package com.xinto.opencord.network.api

import com.xinto.opencord.network.response.ApiGuild
import retrofit2.http.GET

interface DiscordAPI {

    @GET("users/@me/guilds")
    fun getUserGuilds(): List<ApiGuild>

}