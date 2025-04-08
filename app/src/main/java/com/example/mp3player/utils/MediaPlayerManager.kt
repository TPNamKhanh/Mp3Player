package com.example.mp3player.utils

import android.media.AudioAttributes
import android.media.MediaPlayer

class MediaPlayerManager {
    var mediaPlayer: MediaPlayer? = null
    val currentPosition get() = mediaPlayer?.currentPosition

    init {
        val attribute = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA).build()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioAttributes(attribute)
    }


    fun setupData(path: String) {
        mediaPlayer?.apply {
            reset()
            setDataSource(path)
            prepare()
        }
    }

    fun start() {
        mediaPlayer?.start()
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun release() {
        mediaPlayer?.release()
    }

    fun seekTo(progress: Int) {
        mediaPlayer?.seekTo(progress)
    }
}