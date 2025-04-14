package com.example.mp3player.domain.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.bundle.Bundle
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
        val action = intent?.action
        if (action != null) {
            when (action) {
                PLAY_OR_PAUSE_ACTION -> {
                    if (isPlaying) {
                        pauseMp3()
                    } else startAudio()
                }

                PREV_ACTION -> prev()
                NEXT_ACTION -> next()
            }
        } else {
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
        sendAudioItem()
        sendState(PLAY_ACTION)
    }

    private fun sendHandleSeekbarBroadcast() {
        val intent = Intent(PROCESS_SEEKBAR)
        sendBroadcast(intent)
    }

    private fun sendAudioItem() {
        if (items.isNotEmpty() && mCurrentPosition != null) {
            val intent = Intent(UPDATE_ITEM)
            val bundle = Bundle()
            bundle.putSerializable(MAIN_ITEM, items[mCurrentPosition!!])
            intent.putExtras(bundle)
            sendBroadcast(intent)
        }
    }

    private fun sendState(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    fun pauseMp3() {
        if (isPlaying) {
            medialPlayerManager?.pause()
            items[mCurrentPosition!!].isPlaying = false
            isPlaying = false
            sendState(PAUSE_ACTION)
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

    private fun next() {
        if (items.isNotEmpty() && mCurrentPosition != null && mCurrentPosition!! + 1 < items.size) {
            mCurrentPosition = mCurrentPosition!! + 1
            startAudio(isNext = true)
        }
    }

    private fun prev() {
        if (items.isNotEmpty() && mCurrentPosition != null && mCurrentPosition!!.dec() >= 0) {
            mCurrentPosition = mCurrentPosition!!.dec()
            startAudio(isNext = true)
        }
    }

    companion object {
        const val PROCESS_SEEKBAR = "com.example.mp3player.process_seekbar"
        const val UPDATE_ITEM = "com.example.mp3player.update_item"
        const val MEDIA_COMPLETE_KEY = "MediaIsComplete"
        const val MP3_FILE = "mp3_file"
        const val IS_STOP_SERVICE = "is_stop_service"
        const val MAIN_ITEM = "main_item"
        const val PLAY_OR_PAUSE_ACTION = "playOrPause"
        const val NEXT_ACTION = "next"
        const val PREV_ACTION = "prev"
        const val PLAY_ACTION = "play"
        const val PAUSE_ACTION = "pause"
    }
}