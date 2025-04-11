package com.example.mp3player.domain.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.mp3player.domain.model.LocalItem
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