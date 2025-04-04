package com.example.mp3player.di

import com.example.mp3player.data.repository.LocalItemRepositoryIMPL
import com.example.mp3player.domain.repository.ILocalItemRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::LocalItemRepositoryIMPL) { bind<ILocalItemRepository>() }
}