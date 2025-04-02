package com.example.mp3player.presentation.ui.activity

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mp3player.R
import com.example.mp3player.databinding.ActivityMainBinding
import com.example.mp3player.presentation.ui.fragment.MainFragment
import com.example.mp3player.presentation.ui.fragment.ProfileFragment
import com.example.mp3player.presentation.ui.fragment.StorageFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationMenu.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itHome -> replaceFragment(MainFragment())
                R.id.itStorage -> replaceFragment(StorageFragment())
                R.id.itProfile -> replaceFragment(ProfileFragment())
                else -> replaceFragment(MainFragment())
            }
            true
        }
        setColorForBottomNav()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

    private fun setColorForBottomNav() {
        val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        val isDarkMode = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
//        val isDarkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        binding.bottomNavigationMenu.setBackgroundColor(
            ContextCompat.getColor(
                this,
                if (isDarkMode) R.color.black else R.color.white
            )
        )
    }
}