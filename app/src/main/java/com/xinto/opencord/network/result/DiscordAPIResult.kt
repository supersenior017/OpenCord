package com.xinto.opencord.network.result

import retrofit2.HttpException

sealed class DiscordAPIResult<out T> {

    data class Success<out V>(val result: V) : DiscordAPIResult<V>()
    data class Error(val e: HttpException) : DiscordAPIResult<Nothing>()

}
