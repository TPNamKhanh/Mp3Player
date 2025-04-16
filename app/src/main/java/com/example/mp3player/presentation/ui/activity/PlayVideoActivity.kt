package com.example.mp3player.presentation.ui.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mp3player.R
import com.example.mp3player.domain.services.AudioService
import com.example.mp3player.presentation.ui.fragment.PlayVideoFragment

class PlayVideoActivity : AppCompatActivity() {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action != null && action == MOVE_TASK_TO_BACK_VIDEO) {
                moveTaskToBack(true)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        stopMp3()
        changeDestination()
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter().apply {
            addAction(MOVE_TASK_TO_BACK_VIDEO)
        }
        ContextCompat.registerReceiver(
            this,
            receiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(receiver)
    }

    private fun stopMp3() {
        val intent = Intent(this, AudioService::class.java)
        intent.action = AudioService.IS_STOP_SERVICE
        startService(intent)
    }

    @SuppressLint("CommitTransaction")
    private fun changeDestination() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frPlayVideo, PlayVideoFragment())
        transaction.commit()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        val fragment = supportFragmentManager.findFragmentById(R.id.frPlayVideo)
        if (fragment is PlayVideoFragment) {
            fragment.enterPipModeFromFragment()
        }
    }

    companion object {
        const val MOVE_TASK_TO_BACK_VIDEO = "move_task_to_back_video"
    }
}