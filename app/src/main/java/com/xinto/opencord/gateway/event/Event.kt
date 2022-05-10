package com.xinto.opencord.gateway.event

import com.xinto.opencord.gateway.dto.MessageDeleteData
import com.xinto.opencord.gateway.dto.Ready
import com.xinto.opencord.gateway.io.EventName
import com.xinto.opencord.rest.dto.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonElement

interface Event

class EventDeserializationStrategy(
    private val eventName: EventName
) : DeserializationStrategy<Event> {

    override val descriptor: SerialDescriptor
        get() = JsonElement.serializer().descriptor

    override fun deserialize(decoder: Decoder): Event {
        return when (eventName) {
            EventName.Ready -> {
                ReadyEvent(
                    data = decoder.decodeSerializableValue(Ready.serializer())
                )
            }
            EventName.GuildMemberChunk -> {
                GuildMemberChunkEvent(
                    data = decoder.decodeSerializableValue(ApiGuildMemberChunk.serializer())
                )
            }
            EventName.GuildCreate -> {
                GuildCreateEvent(
                    data = decoder.decodeSerializableValue(ApiGuild.serializer())
                )
            }
            EventName.GuildUpdate -> {
                GuildUpdateEvent(
                    data = decoder.decodeSerializableValue(ApiGuild.serializer())
                )
            }
            EventName.GuildDelete -> {
                TODO()
            }
            EventName.ChannelCreate -> {
                ChannelCreateEvent(
                    data = decoder.decodeSerializableValue(ApiChannel.serializer())
                )
            }
            EventName.ChannelUpdate -> {
                ChannelUpdateEvent(
                    data = decoder.decodeSerializableValue(ApiChannel.serializer())
                )
            }
            EventName.ChannelDelete -> {
                ChannelDeleteEvent(
                    data = decoder.decodeSerializableValue(ApiChannel.serializer())
                )
            }
            EventName.MessageCreate -> {
                MessageCreateEvent(
                    data = decoder.decodeSerializableValue(ApiMessage.serializer())
                )
            }
            EventName.MessageUpdate -> {
                MessageUpdateEvent(
                    data = decoder.decodeSerializableValue(ApiMessagePartial.serializer())
                )
            }
            EventName.MessageDelete -> {
                MessageDeleteEvent(
                    data = decoder.decodeSerializableValue(MessageDeleteData.serializer())
                )
            }
            EventName.UserSettingsUpdate -> {
                UserSettingsUpdateEvent(
                    data = decoder.decodeSerializableValue(ApiUserSettingsPartial.serializer())
                )
            }
            else -> throw IllegalArgumentException("Unknown event $eventName")
        }
    }
}