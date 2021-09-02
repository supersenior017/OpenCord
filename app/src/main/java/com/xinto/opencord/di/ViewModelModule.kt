package com.xinto.opencord.di

import com.xinto.opencord.network.repository.DiscordAPIRepository
import com.xinto.opencord.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    fun getMainViewModel(
        repository: DiscordAPIRepository
    ) = MainViewModel(repository)

    viewModel { getMainViewModel(get()) }

}