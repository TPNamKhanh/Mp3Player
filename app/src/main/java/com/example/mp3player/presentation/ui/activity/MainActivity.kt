package com.example.mp3player.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}