package com.xinto.opencord.domain.activity.types

import androidx.compose.runtime.Immutable
import com.xinto.opencord.domain.activity.*

@Immutable
data class DomainActivityGame(
    override val name: String,
    override val createdAt: Long,
    val id: String?,
    val state: String,
    val details: String,
    val applicationId: Long,
    val party: DomainActivityParty?,
    val assets: DomainActivityAssets?,
    val secrets: DomainActivitySecrets?,
    val timestamps: DomainActivityTimestamp?,
) : DomainActivity {
    override val type = ActivityType.Game
}
