package com.example.mp3player.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.mp3player.di.moduleApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

@OptIn(KoinExperimentalAPI::class)
class Mp3PlayerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        GlobalContext.startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@Mp3PlayerApplication)
            fragmentFactory()
            modules(moduleApp)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "notification"
        const val CHANNEL_NAME = "mp3_notification"
    }
}