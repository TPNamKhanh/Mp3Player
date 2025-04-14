package com.example.mp3player.presentation.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mp3player.R
import com.example.mp3player.databinding.ActivityMainBinding
import com.example.mp3player.databinding.BottomMediaLayoutBinding
import com.example.mp3player.domain.model.LocalItem
import com.example.mp3player.domain.services.AudioService
import com.example.mp3player.presentation.ui.fragment.MainFragment
import com.example.mp3player.presentation.ui.fragment.ProfileFragment
import com.example.mp3player.presentation.ui.fragment.StorageFragment

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomMediaBinding: BottomMediaLayoutBinding
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions -> }
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (action != null) {
                when (action) {
                    AudioService.PLAY_ACTION -> {
                        bottomMediaBinding.imgPlayOrPause.setImageResource(R.drawable.ic_pause_circle)
                    }

                    AudioService.PAUSE_ACTION -> {
                        bottomMediaBinding.imgPlayOrPause.setImageResource(R.drawable.ic_play_circle)
                    }

                    AudioService.UPDATE_ITEM -> {
                        val bundle = intent.extras
                        if (bundle != null) {
                            val item = bundle.getSerializable(AudioService.MAIN_ITEM) as LocalItem
                            with(bottomMediaBinding) {
                                tvAudioTitle.text = item.name
                                tvAuthorName.text = item.author
                                llAudioInfo.setOnClickListener { startPlayMp3Activity() }
                                root.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bottomMediaBinding = binding.bottomMediaNotificationLayout
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
        with(bottomMediaBinding) {
            imgPlayOrPause.setOnClickListener {
                musicAction(
                    AudioService.PLAY_OR_PAUSE_ACTION,
                    this@MainActivity
                )
            }
            imgSkipPrev.setOnClickListener {
                musicAction(
                    AudioService.PREV_ACTION,
                    this@MainActivity
                )
            }
            imgSkipNext.setOnClickListener {
                musicAction(
                    AudioService.NEXT_ACTION,
                    this@MainActivity
                )
            }
        }
        handleSwipeForBottomNotification()
        setColorForBottomNav()
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter().apply {
            addAction(AudioService.PLAY_ACTION)
            addAction(AudioService.PAUSE_ACTION)
            addAction(AudioService.UPDATE_ITEM)
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

    @SuppressLint("ClickableViewAccessibility")
    private fun handleSwipeForBottomNotification() {
        bottomMediaBinding.rlBottomNavigationLayout.setOnTouchListener(object :
            View.OnTouchListener {
            private var downX = 0f
            private var isSwiping = false
            override fun onTouch(
                v: View?,
                event: MotionEvent?
            ): Boolean {
                if (v == null || event == null) return false
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downX = event.x
                        isSwiping = true
                        return true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if (isSwiping) {
                            val deltaX = event.x - downX
                            v.translationX = deltaX
                        }
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        if (isSwiping) {
                            val deltaX = event.x - downX
                            val swipeThreshold = v.width / 50
                            val screenWith = resources.displayMetrics.widthPixels
                            if (Math.abs(deltaX) > swipeThreshold) {
                                val targetX =
                                    if (deltaX > 0) screenWith.toFloat() else -screenWith.toFloat()
                                v.animate()
                                    .translationX(targetX)
                                    .setDuration(200)
                                    .withEndAction {
                                        v.translationX = 0f
                                        bottomMediaBinding.rlBottomNavigationLayout.visibility =
                                            View.GONE
                                        stopService()
                                    }.start()
                            }
                        }
                    }
                }
                return false
            }
        })
    }

    fun stopService() {
        val intent = Intent(this, AudioService::class.java)
        val bundle = Bundle()
        bundle.putBoolean(AudioService.IS_STOP_SERVICE, true)
        intent.putExtras(bundle)
        startService(intent)
    }

    fun musicAction(action: String, context: Context) {
        val intent = Intent(context, AudioService::class.java)
        intent.action = action
        context.startService(intent)
    }

    fun startPlayMp3Activity() {
        val intent = Intent(this, PlayMp3Activity::class.java)
        intent.action = "IS_AUDIO"
        startActivity(intent)
    }

}