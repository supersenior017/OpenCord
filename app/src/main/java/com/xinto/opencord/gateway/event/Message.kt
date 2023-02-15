package com.xinto.opencord.gateway.event

import com.xinto.opencord.gateway.dto.MessageAckData
import com.xinto.opencord.gateway.dto.MessageDeleteData
import com.xinto.opencord.rest.models.message.ApiMessage
import com.xinto.opencord.rest.models.message.ApiMessagePartial

data class MessageCreateEvent(
    val data: ApiMessage,
) : Event

data class MessageUpdateEvent(
    val data: ApiMessagePartial,
) : Event

data class MessageDeleteEvent(
    val data: MessageDeleteData,
) : Event

data class MessageAckEvent(
    val data: MessageAckData,
) : Event
