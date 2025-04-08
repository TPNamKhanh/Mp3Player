package com.example.mp3player.presentation.ui.activity

import android.Manifest
import android.app.UiModeManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mp3player.R
import com.example.mp3player.databinding.ActivityMainBinding
import com.example.mp3player.presentation.ui.fragment.MainFragment
import com.example.mp3player.presentation.ui.fragment.ProfileFragment
import com.example.mp3player.presentation.ui.fragment.StorageFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermission()
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

    private fun checkPermission() {
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arrayOf(
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_VIDEO
            )
            else arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

        val notGrantedPermisison = permission.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (notGrantedPermisison.isNotEmpty()) {
            requestPermissionLauncher.launch(permission)
        }
    }
}