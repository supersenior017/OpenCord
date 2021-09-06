package com.xinto.opencord.domain.model

import com.xinto.opencord.domain.model.base.DomainResponse
import com.xinto.opencord.network.response.ApiMessage

data class DomainMessage(
    val id: Long,
    val channelId: Long,
    val content: String,
    val author: DomainMessageAuthor,
) : DomainResponse {

    companion object {

        fun fromApi(
            apiMessage: ApiMessage
        ) = with(apiMessage) {
            DomainMessage(
                id = id,
                content = content,
                channelId = channelId,
                author = DomainMessageAuthor.fromApi(author)
            )
        }

    }

}
