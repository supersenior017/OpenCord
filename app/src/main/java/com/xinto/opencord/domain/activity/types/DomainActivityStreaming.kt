package com.xinto.opencord.domain.activity.types

import androidx.compose.runtime.Immutable
import com.xinto.opencord.domain.activity.ActivityType
import com.xinto.opencord.domain.activity.DomainActivity
import com.xinto.opencord.domain.activity.DomainActivityAssets

@Immutable
data class DomainActivityStreaming(
    override val name: String,
    override val createdAt: Long,
    val id: String,
    val url: String,
    val state: String,
    val details: String,
    val assets: DomainActivityAssets,
) : DomainActivity {
    override val type = ActivityType.Streaming
}
