package com.example.mp3player.app

import android.app.Application
import android.util.Log
import com.example.mp3player.di.moduleApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.lazyModules
import org.koin.core.logger.Level
import kotlin.math.log

@OptIn(KoinExperimentalAPI::class)
class Mp3PlayerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("TAG", "onCreate: StartKoin")
        GlobalContext.startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@Mp3PlayerApplication)
            fragmentFactory()
            Log.d("TAG", "onCreate: inside koin")
            modules(moduleApp)
            Log.d("TAG", "onCreate: before lazy koin")
//            modules(lazyModuleApp)
        }
        Log.d("TAG", "onCreate: FinishKoin")
    }
}