package com.xinto.opencord.domain.manager

import android.content.SharedPreferences
import com.xinto.opencord.domain.manager.base.BasePreferenceManager

interface PersistentDataManager {

    var currentGuildId: Long

    var currentChannelId: Long

}

class PersistentDataManagerImpl(
    persistentPrefs: SharedPreferences
) : BasePreferenceManager(persistentPrefs), PersistentDataManager {

    override var currentGuildId: Long
        get() = getLong(CURRENT_GUILD_ID_KEY, CURRENT_GUILD_ID_DEFAULT)
        set(value) {
            putLong(CURRENT_GUILD_ID_KEY, value)
        }

    override var currentChannelId: Long
        get() = getLong(CURRENT_CHANNEL_ID_KEY, CURRENT_CHANNEL_ID_DEFAULT)
        set(value) {
            putLong(CURRENT_CHANNEL_ID_KEY, value)
        }

    companion object {
        const val CURRENT_GUILD_ID_KEY = "current_guild_id"
        const val CURRENT_GUILD_ID_DEFAULT = 0L

        const val CURRENT_CHANNEL_ID_KEY = "current_channel_id"
        const val CURRENT_CHANNEL_ID_DEFAULT = 0L
    }
}