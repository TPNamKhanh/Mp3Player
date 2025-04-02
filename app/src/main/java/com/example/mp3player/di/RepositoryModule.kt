package com.example.mp3player.di

import com.example.mp3player.data.repository.LocalAudioRepositoryIMPL
import com.example.mp3player.domain.repository.ILocalAudioRepository
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.lazyModule
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::LocalAudioRepositoryIMPL) { bind<ILocalAudioRepository>() }
}