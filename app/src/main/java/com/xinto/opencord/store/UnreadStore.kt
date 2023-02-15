package com.xinto.opencord.store

import androidx.room.withTransaction
import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.db.entity.channel.EntityUnreadState
import com.xinto.opencord.domain.channel.DomainUnreadState
import com.xinto.opencord.domain.channel.toDomain
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.ChannelDeleteEvent
import com.xinto.opencord.gateway.event.MessageAckEvent
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.service.DiscordApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter

typealias UnreadEvent = Event<DomainUnreadState, Nothing, Long>

interface UnreadStore {
    fun observeChannel(channelId: Long): Flow<UnreadEvent>

    suspend fun getChannel(channelId: Long): DomainUnreadState?
}

class UnreadStoreImpl(
    gateway: DiscordGateway,
    private val api: DiscordApiService,
    private val cache: CacheDatabase,
) : UnreadStore {
    private val events = MutableSharedFlow<UnreadEvent>()

    override fun observeChannel(channelId: Long): Flow<UnreadEvent> {
        return events.filter { event ->
            event.fold(
                onAdd = { it.channelId == channelId },
                onUpdate = { false },
                onDelete = { it == channelId },
            )
        }
    }

    override suspend fun getChannel(channelId: Long): DomainUnreadState? {
        return cache.unreadStates().getUnreadState(channelId)?.toDomain()
    }

    init {
        gateway.onEvent<ReadyEvent> { event ->
            val states = event.data.readState.entries.map {
                EntityUnreadState(
                    channelId = it.channelId,
                    mentionCount = it.mentionCount,
                    lastMessageId = it.lastMessageId,
                )
            }

            states.forEach { events.emit(UnreadEvent.Add(it.toDomain())) }
            cache.withTransaction {
                cache.unreadStates().clear()
                cache.unreadStates().insertUnreadStates(states)
            }
        }

        gateway.onEvent<MessageAckEvent> { event ->
            val state = EntityUnreadState(
                channelId = event.data.channelId,
                mentionCount = event.data.mentionCount ?: 0,
                lastMessageId = event.data.messageId,
            )

            events.emit(UnreadEvent.Add(state.toDomain()))
            cache.unreadStates().insertUnreadState(state)
        }

        gateway.onEvent<ChannelDeleteEvent> { event ->
            val channelId = event.data.id.value

            events.emit(UnreadEvent.Delete(channelId))
            cache.unreadStates().deleteUnreadState(channelId)
        }
    }
}
