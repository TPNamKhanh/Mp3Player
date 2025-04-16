package com.example.mp3player.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mp3player.databinding.FragmentLocalVideoBinding
import com.example.mp3player.domain.model.AudioData
import com.example.mp3player.presentation.adapter.LocalItemAdapter
import com.example.mp3player.presentation.ui.activity.PlayVideoActivity
import com.example.mp3player.presentation.viewmodel.StorageViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocalVideoFragment : Fragment() {
    private lateinit var binding: FragmentLocalVideoBinding
    private lateinit var adapter: LocalItemAdapter
    private val viewModel: StorageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocalVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvVideoList.addItemDecoration(LocalAudioFragment.Companion.addDivider(requireContext()))
            lifecycleScope.launch {
                viewModel.videos.collectLatest { list ->
                    adapter = LocalItemAdapter(emptyList()) { position ->
                        EventBus.getDefault().postSticky(AudioData(position, list))
                        startVideoActivity()
                    }
                    rvVideoList.adapter = adapter
                    if (!list.isEmpty()) {
                        adapter.updateItem(list)
                    }
                }
            }
        }
    }

    private fun startVideoActivity() {
        val intent = Intent(requireContext(), PlayVideoActivity::class.java)
        startActivity(intent)
    }
}