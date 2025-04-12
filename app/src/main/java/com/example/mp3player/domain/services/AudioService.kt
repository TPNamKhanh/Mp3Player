package com.example.mp3player.domain.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.session.MediaSession
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mp3player.R
import com.example.mp3player.app.Mp3PlayerApplication
import com.example.mp3player.domain.model.LocalItem
import com.example.mp3player.presentation.receiver.MediaReceiver
import com.example.mp3player.utils.MediaPlayerManager

@Suppress("DEPRECATION")
class AudioService : Service() {
    private val mBinder = Mp3Binder()
    private var mCurrentPosition: Int? = null
    private var medialPlayerManager: MediaPlayerManager? = null
    private var items = listOf<LocalItem>()
    var isPlaying: Boolean = false

    inner class Mp3Binder : Binder() {
        fun getService(): AudioService = this@AudioService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle = intent?.extras
        if (bundle != null) {
            val isStopService = bundle.getBoolean(IS_STOP_SERVICE, false)
            if (isStopService) {
                stopAudioService()
            }
            val currentPosition = bundle.getSerializable(MP3_FILE) as Int?
            if (currentPosition != null) {
                var isNext = false
                if (currentPosition != mCurrentPosition) {
                    mCurrentPosition = currentPosition
                    isNext = true
                }
                createNotification()
                startAudio(isNext)
                sendHandleSeekbarBroadcast()
            }
        }
        return START_NOT_STICKY
    }

    private fun stopAudioService() {
        isPlaying = false
        medialPlayerManager?.release()
        medialPlayerManager = null
    }

    private fun startAudio(isNext: Boolean = false) {
        if (items.isEmpty()) return
        if (medialPlayerManager == null) {
            medialPlayerManager = MediaPlayerManager()
        }
        medialPlayerManager?.apply {
            if (isNext) {
                setupData(items[mCurrentPosition ?: 0].data)
            }
            start()
        }
        isPlaying = true
    }

    private fun sendHandleSeekbarBroadcast() {
        val intent = Intent(PROCESS_SEEKBAR)
        sendBroadcast(intent)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @SuppressLint("ForegroundServiceType")
    private fun createNotification() {
        if (mCurrentPosition == null || items.isEmpty()) return
        val mediaSession = MediaSessionCompat(this, "mp3_notification")

        val notificationStyle = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0)
            .setMediaSession(mediaSession.sessionToken)

        val playOrPauseIntent = Intent(this, MediaReceiver::class.java).apply {
            action = "ACTION_PAUSE"
        }

        val playOrPausePendingIntent = PendingIntent.getBroadcast(
            this, 0, playOrPauseIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this)
            .setChannelId(Mp3PlayerApplication.CHANNEL_ID)
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText(items[mCurrentPosition ?: 0].name)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            .setSubText("231232")
            .addAction(R.drawable.ic_pause_circle, "Pause", playOrPausePendingIntent)
            .setStyle(notificationStyle)
            .setOnlyAlertOnce(true)
            .build()

        val managerCompat = NotificationManagerCompat.from(this)
//        managerCompat.notify(1, notification)
        startForeground(1, notification)
    }

    fun pauseMp3() {
        if (isPlaying) {
            medialPlayerManager?.pause()
            isPlaying = false
        }
    }

    fun getCurrentPosition(): Int {
        return medialPlayerManager?.currentPosition ?: 0
    }

    fun seekTo(progress: Int) {
        medialPlayerManager?.seekTo(progress)
    }

    fun setup(items: List<LocalItem>) {
        this.items = items
    }

    companion object {
        const val PROCESS_SEEKBAR = "com.example.mp3player.process_seekbar"
        const val MEDIA_COMPLETE_KEY = "MediaIsComplete"
        const val MP3_FILE = "mp3_file"
        const val IS_STOP_SERVICE = "is_stop_service"
    }
}