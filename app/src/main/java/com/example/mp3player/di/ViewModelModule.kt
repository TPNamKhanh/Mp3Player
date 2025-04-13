@file:Suppress("DEPRECATION")

package com.example.mp3player.di

import com.example.mp3player.presentation.viewmodel.MainViewModel
import com.example.mp3player.presentation.viewmodel.PlayMp3ViewModel
import com.example.mp3player.presentation.viewmodel.PlayVideoViewModel
import com.example.mp3player.presentation.viewmodel.StorageViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::StorageViewModel)
    viewModelOf(::PlayMp3ViewModel)
    viewModelOf(::PlayVideoViewModel)
    viewModelOf(::MainViewModel)
}