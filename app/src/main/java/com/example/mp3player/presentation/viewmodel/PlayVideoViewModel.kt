package com.example.mp3player.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.mp3player.domain.model.AudioData
import com.example.mp3player.domain.model.LocalItem

class PlayVideoViewModel() : ViewModel() {
    private var videos: List<LocalItem> = listOf()
    private var currentPosition: Int? = null
    var exoPlayer: ExoPlayer? = null
    var isNext = false

    fun updateVideos(data: AudioData) {
        isNext = currentPosition != data.position
        videos = data.items
        currentPosition = data.position
    }

    fun initExoPlayer(context: Context) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
    }

    fun releaseExoPlayer() {
        exoPlayer?.apply {
            stop()
            clearMediaItems()
            release()
        }
        exoPlayer = null
    }

    fun startVideo() {
        if (isNext) {
            val item = videos[currentPosition ?: 0]
            val mediaItem = MediaItem.fromUri(item.data)
            exoPlayer?.apply {
                stop()
                clearMediaItems()
                setMediaItem(mediaItem)
                prepare()
            }
        }
    }
}