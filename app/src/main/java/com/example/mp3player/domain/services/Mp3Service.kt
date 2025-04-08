package com.example.mp3player.domain.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.mp3player.domain.model.LocalItem
import com.example.mp3player.utils.MediaPlayerManager

@Suppress("DEPRECATION")
class Mp3Service : Service() {
    private val mBinder = Mp3Binder()
    private var mCurrentPosition: Int? = null
    private var medialPlayerManager: MediaPlayerManager? = null
    private var items = listOf<LocalItem>()
    var isPlaying: Boolean = false

    inner class Mp3Binder : Binder() {
        fun getService(): Mp3Service = this@Mp3Service
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle = intent?.extras
        if (bundle != null) {
            val currentPosition = bundle.getSerializable("mp3_file") as Int?
            Log.d("TAG", "onStartCommand: $currentPosition -- $items")
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
    }
}