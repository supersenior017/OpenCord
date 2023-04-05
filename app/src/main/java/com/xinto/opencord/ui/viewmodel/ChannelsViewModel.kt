package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.channel.DomainCategoryChannel
import com.xinto.opencord.domain.channel.DomainChannel
import com.xinto.opencord.domain.channel.DomainUnreadState
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.store.*
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import com.xinto.opencord.util.collectIn
import kotlinx.coroutines.*


@Stable
class ChannelsViewModel(
    persistentDataManager: PersistentDataManager,
    private val channelStore: ChannelStore,
    private val guildStore: GuildStore,
    private val lastMessageStore: LastMessageStore,
    private val unreadStore: UnreadStore,
) : BasePersistenceViewModel(persistentDataManager) {
    sealed interface State {
        object Unselected : State
        object Loading : State
        object Loaded : State
        object Error : State
    }

    @Stable
    class ChannelItemData(
        channel: DomainChannel,
        mentionCount: Int,
        var unreadListenerJob: Job? = null,
        var lastMessageListenerJob: Job? = null,
        var mentionCountListenerJob: Job? = null,
        private var lastUnreadMessageId: Long? = null,
        private var lastChannelMessageId: Long? = null,
    ) {
        private val _isUnread: Boolean
            get() = (lastChannelMessageId ?: 0) > (lastUnreadMessageId ?: 0)

        var channel by mutableStateOf(channel)
        var mentionCount by mutableStateOf(mentionCount)
        var isUnread by mutableStateOf(_isUnread)
            private set

        fun updateUnreadState(unreadState: DomainUnreadState?) {
            lastUnreadMessageId = unreadState?.lastMessageId
            mentionCount = unreadState?.mentionCount ?: 0
            isUnread = _isUnread
        }

        fun updateLastMessageId(lastMessageId: Long?) {
            this.lastChannelMessageId = lastMessageId
            isUnread = _isUnread
        }

        fun cancelJobs() {
            unreadListenerJob?.cancel()
            lastMessageListenerJob?.cancel()
            mentionCountListenerJob?.cancel()
        }
    }

    @Stable
    class CategoryItemData(
        channel: DomainCategoryChannel,
        collapsed: Boolean,
        subChannels: List<ChannelItemData>?,
    ) {
        var channel by mutableStateOf(channel)
        var collapsed by mutableStateOf(collapsed)
        var channels = mutableStateMapOf<Long, ChannelItemData>()

        val channelsSorted by derivedStateOf {
            channels.values.sortedWith { a, b -> a.channel compareTo b.channel }
        }

        init {
            if (subChannels != null) {
                channels.putAll(subChannels.associateBy { it.channel.id })
            }
        }
    }

    var state by mutableStateOf<State>(State.Unselected)
        private set

    var selectedChannelId by mutableStateOf(0L)
        private set

    var guildName by mutableStateOf("")
        private set
    var guildBannerUrl by mutableStateOf<String?>(null)
        private set
    var guildBoostLevel by mutableStateOf(0)
        private set

    val categoryChannels = mutableStateMapOf<Long, CategoryItemData>()
    val noCategoryChannels = mutableStateMapOf<Long, ChannelItemData>()

    // Dual reference to all channel items for events updating state
    private val allChannelItems = mutableMapOf<Long, ChannelItemData>()

    fun load() {
        if (persistentGuildId <= 0L) return

        viewModelScope.coroutineContext.cancelChildren()
        viewModelScope.launch {
            state = State.Loading
            withContext(Dispatchers.IO) {
                try {
                    val guild = guildStore.fetchGuild(persistentGuildId)
                        ?: return@withContext

                    withContext(Dispatchers.Main) {
                        guildName = guild.name
                        guildBannerUrl = guild.bannerUrl
                        guildBoostLevel = guild.premiumTier
                    }

                    replaceChannels(channelStore.fetchChannels(persistentGuildId))
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        state = State.Error
                    }
                }
            }
        }

        guildStore.observeGuild(persistentGuildId).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = {
                    guildName = it.name
                    guildBannerUrl = it.bannerUrl
                    guildBoostLevel = it.premiumTier
                },
                onUpdate = {
                    guildName = it.name
                    guildBannerUrl = it.bannerUrl
                    guildBoostLevel = it.premiumTier
                },
                onDelete = {
                    state = State.Unselected
                },
            )
        }

        channelStore.observeChannelsReplace(persistentGuildId).collectIn(viewModelScope) { channels ->
            replaceChannels(channels)
        }

        channelStore.observeChannels(persistentGuildId).collectIn(viewModelScope) { event ->
            state = State.Loaded
            event.fold(
                onAdd = { channel ->
                    if (channel is DomainCategoryChannel) {
                        categoryChannels[channel.id] = CategoryItemData(
                            channel = channel,
                            collapsed = false,
                            subChannels = null,
                        )
                    } else {
                        val item = makeAliveChannelItem(channel)

                        // Remove from old category
                        if (allChannelItems[channel.id]?.channel?.parentId != null) {
                            categoryChannels[channel.id]?.channels?.remove(channel.id)
                        }

                        allChannelItems[channel.id] = item
                        channel.parentId?.let { categoryChannels[it]?.channels?.set(it, item) }
                    }
                },
                onUpdate = { channel ->
                    if (channel is DomainCategoryChannel) {
                        categoryChannels.compute(channel.id) { _, categoryItem ->
                            categoryItem?.apply {
                                this.channel = channel
                            } ?: CategoryItemData(
                                channel = channel,
                                collapsed = false,
                                subChannels = allChannelItems.values.filter { channelItem ->
                                    if (channelItem.channel is DomainCategoryChannel)
                                        false
                                    else {
                                        channelItem.channel.parentId == channel.id
                                    }
                                },
                            )
                        }
                    } else {
                        allChannelItems[channel.id]?.channel = channel
                    }
                },
                onDelete = {
                    val categoryId = allChannelItems[it]?.channel?.parentId

                    if (categoryId != null) {
                        allChannelItems.remove(it)
                        categoryChannels[categoryId]?.channels?.remove(it)
                    }
                },
            )
        }
    }

    private suspend fun makeAliveChannelItem(channel: DomainChannel): ChannelItemData {
        if (channel is DomainCategoryChannel) {
            error("cannot make channel item from category channel")
        }

        val unreadState = unreadStore.getChannel(channel.id)
        val item = ChannelItemData(
            channel = channel,
            mentionCount = unreadState?.mentionCount ?: 0,
            lastUnreadMessageId = unreadState?.lastMessageId,
            lastChannelMessageId = lastMessageStore.getLastMessageId(channel.id),
        )

        item.unreadListenerJob = unreadStore.observeUnreadState(channel.id).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = item::updateUnreadState,
                onUpdate = { },
                onDelete = {
                    val categoryId = item.channel.parentId

                    if (categoryId != null) {
                        allChannelItems.remove(it)
                        categoryChannels[categoryId]?.channels?.remove(it)
                        item.cancelJobs()
                    }
                },
            )
        }

        item.lastMessageListenerJob = lastMessageStore.observeChannel(channel.id).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = { (_, messageId) -> item.updateLastMessageId(messageId) },
                onUpdate = { },
                onDelete = { /* Handled by UnreadStore listener */ },
            )
        }

        item.mentionCountListenerJob = unreadStore.observeMentionCount(channel.id).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = { item.mentionCount = it },
                onUpdate = { item.mentionCount += it },
                onDelete = {},
            )
        }

        return item
    }

    private suspend fun replaceChannels(channels: List<DomainChannel>) {
        val (categoryItems, channelItems) = channels
            .partition { it is DomainCategoryChannel }
            .let { (newCategories, newChannels) ->
                val channelItems = newChannels.associate {
                    it.id to makeAliveChannelItem(it)
                }

                val categoryItems = newCategories.associate { category ->
                    category.id to CategoryItemData(
                        channel = category as DomainCategoryChannel,
                        collapsed = persistentCollapsedCategories.contains(category.id),
                        subChannels = channelItems.values.filter {
                            if (it.channel is DomainCategoryChannel)
                                false
                            else {
                                it.channel.parentId == category.id
                            }
                        },
                    )
                }

                categoryItems to channelItems
            }

        val noCategoryItems = channelItems
            .filterValues { it.channel.parentId == null }

        withContext(Dispatchers.Main) {
            synchronized(state) { // Needed or the old cached items can overwrite the new items from gw
                allChannelItems.values.forEach { it.cancelJobs() }
                allChannelItems.clear()
                allChannelItems.putAll(channelItems)

                categoryChannels.clear()
                categoryChannels.putAll(categoryItems)

                noCategoryChannels.clear()
                noCategoryChannels.putAll(noCategoryItems)
                state = State.Loaded
            }
        }
    }

    fun selectChannel(channelId: Long) {
        selectedChannelId = channelId
        persistentChannelId = channelId
    }

    fun toggleCategory(categoryId: Long) {
        if (persistentCollapsedCategories.contains(categoryId))
            removePersistentCollapseCategory(categoryId)
        else {
            addPersistentCollapseCategory(categoryId)
        }

        categoryChannels[categoryId]?.apply { collapsed = !collapsed }
    }

    init {
        if (persistentGuildId != 0L) {
            load()
        }
        if (persistentChannelId != 0L) {
            selectedChannelId = persistentChannelId
        }
    }
}
