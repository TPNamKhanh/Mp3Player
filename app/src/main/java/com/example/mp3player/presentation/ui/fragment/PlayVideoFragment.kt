package com.example.mp3player.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mp3player.databinding.FragmentPlayVideoBinding
import com.example.mp3player.domain.model.AudioData
import com.example.mp3player.presentation.viewmodel.PlayVideoViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayVideoFragment : Fragment() {
    private lateinit var binding: FragmentPlayVideoBinding
    private val viewModel: PlayVideoViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initExoPlayer(requireContext())
        with(binding) {
            pvPlayAudio.player = viewModel.exoPlayer
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releaseExoPlayer()
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun getVideos(videos: AudioData) {
        viewModel.updateVideos(videos)
        viewModel.startVideo()
    }
}