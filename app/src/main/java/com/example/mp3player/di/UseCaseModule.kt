package com.example.mp3player.di

import com.example.mp3player.domain.usecase.LocalItemUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::LocalItemUseCase)
}