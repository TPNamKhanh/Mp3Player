package com.example.mp3player.di

import com.example.mp3player.domain.usecase.LocalAudioUseCase
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.lazyModule
import org.koin.dsl.module


val useCaseModule = module {
    factoryOf(::LocalAudioUseCase)
}