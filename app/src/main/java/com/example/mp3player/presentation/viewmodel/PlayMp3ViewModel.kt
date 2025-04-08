package com.example.mp3player.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mp3player.domain.model.LocalItem
import com.example.mp3player.domain.services.Mp3Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class PlayMp3ViewModel() : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    private var mp3Service = Mp3Service()
    private var currentPosition: Int? = null
    private var items: List<LocalItem> = listOf()
    private var isConnected: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    val isLastItem = MutableStateFlow(false)
    val isFirstItem = MutableStateFlow(false)
    val currentProgress = MutableStateFlow(0)
    var isPlaying = false
    var info = MutableStateFlow(LocalItem(0, "", "", "", "", 0))
    private val runnable = object : Runnable {
        override fun run() {
            if (mp3Service.isPlaying) {
                mp3Service.let {
                    currentProgress.value = it.getCurrentPosition()
                    handler.postDelayed(this, 500)
                }
            }
        }
    }

    private lateinit var serviceConnection: PlayMp3ServiceConnection

    fun bindMp3Service(context: Context) {
        val intent = Intent(context, Mp3Service::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun start(
        context: Context,
    ) {
        if (currentPosition == null) return
        isPlaying = true
        val intent = Intent(context, Mp3Service::class.java)
        val bundle = Bundle()
        bundle.putSerializable("mp3_file", currentPosition!!)
        intent.putExtras(bundle)
        context.startService(intent)
    }

    fun onNext(context: Context) {
        if (currentPosition != null && currentPosition!!.plus(1) <= items.size.dec()) {
            currentPosition = currentPosition?.plus(1)
        }
        if (isPlaying) {
            start(context)
        }
        updateAudioItem(currentPosition!!)
        unProcessSeekBar()
        currentProgress.value = 0
    }

    fun onPrev(context: Context) {
        if (currentPosition != null && currentPosition!! > 0) {
            currentPosition = currentPosition?.dec()
        }
        if (isPlaying) {
            start(context)
        }
        updateAudioItem(currentPosition!!)
        unProcessSeekBar()
        currentProgress.value = 0
    }

    fun pause() {
        isPlaying = false
        mp3Service.pauseMp3()
    }

    fun processSeekBar() {
        handler.post(runnable)
    }

    fun unProcessSeekBar() {
        handler.removeCallbacks(runnable)
    }

    fun unbindMp3Service(context: Context) {
        context.unbindService(serviceConnection)
        isConnected = false
        handler.removeCallbacks(runnable)
    }

    fun updateAudioItem(position: Int) {
        currentPosition = position
        isLastItem.value = isLastItem()
        isFirstItem.value = isFirstItem()
        val item = items[position]
        info.update { it ->
            it.copy(
                id = item.id,
                name = item.name,
                author = item.author,
                duration = item.duration,
                avatar = item.avatar,
                data = item.data,
            )
        }
    }

    fun getConnectedStatus(): Boolean {
        return isConnected
    }

    fun seekTo(progress: Int) {
        mp3Service.seekTo(progress)
    }

    fun setItems(items: List<LocalItem>) {
        this.items = items
    }

    fun isFirstItem(): Boolean {
        return currentPosition == 0
    }

    fun isLastItem(): Boolean {
        return currentPosition == items.size.dec()
    }

    fun initConnection(context: Context) {
        serviceConnection = PlayMp3ServiceConnection(context)
    }

    @SuppressLint("DefaultLocale")
    fun formatMillis(millis: Int): String {
        val minutes = millis / 1000 / 60
        val second = (millis / 1000) % 60
        return String.format("%02d:%02d", minutes, second)
    }

    private inner class PlayMp3ServiceConnection(private val context: Context) : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            val binder = service as Mp3Service.Mp3Binder
            mp3Service = binder.getService()
            isConnected = true
            mp3Service.setup(items)
            start(context)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
        }
    }
}