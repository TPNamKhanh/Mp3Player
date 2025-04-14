package com.example.mp3player.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mp3player.R
import com.example.mp3player.presentation.ui.fragment.PlayVideoFragment
import com.example.mp3player.presentation.ui.fragment.PlayingMp3Fragment

class PlayMp3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_mp3)
        val action = intent.action
        when(action) {
            "IS_VIDEO" -> changeStartDestination(PlayVideoFragment())
            "IS_AUDIO" -> changeStartDestination(PlayingMp3Fragment())
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        val fragment = supportFragmentManager.findFragmentById(R.id.storage_nav_graph)
        if(fragment is PlayVideoFragment) {
            fragment.enterPipModeFromFragment()
        }
    }

    private fun changeStartDestination(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.storage_nav_graph, fragment)
        fragmentTransaction.commit()
    }

    companion object {
        const val IS_PLAY_VIDEO = "is_play_video"
    }
}