package com.example.mp3player.presentation.ui.fragment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mp3player.R
import com.example.mp3player.databinding.FragmentPlayMp3Binding
import com.example.mp3player.domain.model.AudioData
import com.example.mp3player.domain.services.AudioService
import com.example.mp3player.presentation.viewmodel.PlayMp3ViewModel
import com.example.mp3player.utils.isDarkModeEnabled
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayingMp3Fragment : Fragment() {
    private lateinit var binding: FragmentPlayMp3Binding
    private val viewModel: PlayMp3ViewModel by viewModel()
    private val completeMediaReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                AudioService.PROCESS_SEEKBAR -> {
                    viewModel.processSeekBar()
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayMp3Binding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            if (!isDarkModeEnabled(requireContext())) {
                btnPlayOrPause.setImageResource(R.drawable.ic_pause_circle)
                btnPlayOrPause.setOnClickListener {
                    onPlayOrPauseListener()
                }
                btnBack.setOnClickListener { requireActivity().finish() }
                lifecycleScope.launch {
                    viewModel.currentProgress.collect {
                        sbSeekBar.progress = it
                    }
                }
                lifecycleScope.launch {
                    viewModel.info.collectLatest {
                        tvAudioName.text = it.name
                        tvAuthor.text = it.author
                        sbSeekBar.max = it.duration
                    }
                    onPlayOrPauseListener()
                }
                lifecycleScope.launch {
                    viewModel.isFirstItem.collectLatest { it ->
                        when (it) {
                            true -> {
                                btnSkipPrev.setImageResource(R.drawable.ic_skip_prev_disable)
                            }

                            else -> {
                                btnSkipPrev.setImageResource(R.drawable.ic_skip_previous)
                                btnSkipPrev.setOnClickListener {
                                    sbSeekBar.progress = 0
                                    viewModel.onPrev(
                                        requireContext()
                                    )
                                }
                            }
                        }
                    }
                }
                lifecycleScope.launch {
                    viewModel.isLastItem.collectLatest { it ->
                        when (it) {
                            true -> {
                                btnSkipNext.setImageResource(R.drawable.ic_skip_next_disable)
                            }

                            else -> {
                                btnSkipNext.setImageResource(R.drawable.ic_skip_next)
                                btnSkipNext.setOnClickListener {
                                    sbSeekBar.progress = 0
                                    viewModel.onNext(
                                        requireContext()
                                    )
                                }
                            }
                        }
                    }
                }
                sbSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        if (fromUser) {
                            viewModel.seekTo(progress)
                        }
                        tvDuration.text = "${viewModel.formatMillis(seekBar?.progress ?: 0)}/${
                            viewModel.formatMillis(seekBar?.max ?: 0)
                        }"
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.bindMp3Service(requireContext())
        EventBus.getDefault().register(this)
        val intentFilter = IntentFilter().apply {
            addAction(AudioService.MEDIA_COMPLETE_KEY)
            addAction(AudioService.PROCESS_SEEKBAR)
        }
        ContextCompat.registerReceiver(
            requireContext(),
            completeMediaReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        requireContext().unregisterReceiver(completeMediaReceiver)
        with(viewModel) {
            if (getConnectedStatus()) {
                unbindMp3Service(requireContext())
            }
        }
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onReceiveEvent(data: AudioData) {
        with(binding) {
            viewModel.setItems(data.items)
            val item = data.items[data.position]
            tvAudioName.text = item.name
            tvAuthor.text = item.author
            viewModel.updateAudioItem(data.position)
        }
    }

    fun onPlayOrPauseListener() {
        with(viewModel) {
            if (!isPlaying) {
                binding.btnPlayOrPause.setImageResource(R.drawable.ic_pause_circle)
                start(requireContext())
            } else {
                binding.btnPlayOrPause.setImageResource(R.drawable.ic_play_circle)
                pause()
            }

        }
    }
}