package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.GuildCreateEvent
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import kotlinx.coroutines.launch

class GuildsViewModel(
    gateway: DiscordGateway,
    persistentDataManager: PersistentDataManager,
    private val repository: DiscordApiRepository
) : BasePersistenceViewModel(persistentDataManager) {

    sealed interface State {
        object Loading : State
        object Loaded : State
        object Error : State
    }

    var state by mutableStateOf<State>(State.Loading)
        private set

    val guilds = mutableStateMapOf<Long, DomainGuild>()
    var selectedGuildId by mutableStateOf(0L)
        private set

    fun load() {
        viewModelScope.launch {
            try {
                state = State.Loading
//                val meGuilds = repository.getMeGuilds()
//                guilds.clear()
//                guilds.addAll(meGuilds)
                state = State.Loaded
            } catch (e: Exception) {
                state = State.Error
                e.printStackTrace()
            }
        }
    }

    fun selectGuild(guildId: Long) {
        selectedGuildId = guildId
        persistentGuildId = guildId
    }

    init {
        load()

        gateway.onEvent<ReadyEvent> { event ->
            event.data.guilds.forEach {
                val domainGuild = it.toDomain()
                guilds[domainGuild.id] = domainGuild
            }
        }

        gateway.onEvent<GuildCreateEvent> {
            val domainGuild = it.data.toDomain()
            guilds[domainGuild.id] = domainGuild
        }

        if (persistentGuildId != 0L) {
            selectedGuildId = persistentGuildId
        }
    }

}