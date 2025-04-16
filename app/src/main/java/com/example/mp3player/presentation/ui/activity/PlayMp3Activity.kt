package com.example.mp3player.presentation.ui.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mp3player.R
import com.example.mp3player.presentation.ui.activity.PlayVideoActivity.Companion.MOVE_TASK_TO_BACK_VIDEO

class PlayMp3Activity : AppCompatActivity() {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action != null && action == MOVE_TASK_TO_BACK_MP3) {
                moveTaskToBack(true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_mp3)
        stopVideo()
    }

    private fun stopVideo() {
        val intent = Intent(MOVE_TASK_TO_BACK_VIDEO)
        sendBroadcast(intent)
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter().apply {
            addAction(MOVE_TASK_TO_BACK_MP3)
        }
        ContextCompat.registerReceiver(
            this,
            receiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(receiver)
    }

    companion object {
        const val MOVE_TASK_TO_BACK_MP3 = "move_task_to_back_mp3"
    }
}