package com.example.mp3player.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.includes
import org.koin.dsl.lazyModule
import org.koin.dsl.module

val moduleApp = module {
    includes(viewModelModule, repositoryModule, useCaseModule)
}

//val lazyModuleApp = module {
//    includes(repositoryModule, useCaseModule)
//}