package com.example.mp3player.di

import org.koin.dsl.module

val moduleApp = module {
    includes(viewModelModule, providerModule, repositoryModule, useCaseModule)
}