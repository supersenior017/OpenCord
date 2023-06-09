package com.xinto.opencord.domain.activity.types

import androidx.compose.runtime.Immutable
import com.xinto.opencord.domain.activity.*

@Immutable
data class DomainActivityListening(
    override val name: String,
    override val createdAt: Long,
    val id: String,
    val flags: Int,
    val state: String,
    val details: String,
    val syncId: String,
    val party: DomainActivityParty,
    val assets: DomainActivityAssets,
    val metadata: DomainActivityMetadata?,
    val timestamps: DomainActivityTimestamp,
) : DomainActivity {
    override val type = ActivityType.Listening
}
