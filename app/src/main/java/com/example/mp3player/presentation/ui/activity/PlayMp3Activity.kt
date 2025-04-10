package com.example.mp3player.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mp3player.R
import com.example.mp3player.presentation.ui.fragment.PlayVideoFragment

class PlayMp3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_mp3)
        val bundle = intent.extras
        if (bundle != null) {
            val isPlayVideo = bundle.getBoolean(IS_PLAY_VIDEO, false)
            if (isPlayVideo) {
                changeStartDestination()
            }
        }
    }

    private fun changeStartDestination() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.storage_nav_graph, PlayVideoFragment())
        fragmentTransaction.commit()
    }

    companion object {
        const val IS_PLAY_VIDEO = "is_play_video"
    }
}