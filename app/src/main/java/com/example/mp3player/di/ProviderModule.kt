package com.example.mp3player.di

import android.content.ContentResolver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val providerModule = module {
    single<ContentResolver> { androidContext().contentResolver }
}