package com.example.mp3player.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.mp3player.databinding.FragmentLocalAudioBinding
import com.example.mp3player.domain.model.AudioData
import com.example.mp3player.presentation.adapter.LocalItemAdapter
import com.example.mp3player.presentation.ui.activity.PlayMp3Activity
import com.example.mp3player.presentation.viewmodel.StorageViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocalAudioFragment : Fragment() {
    private lateinit var binding: FragmentLocalAudioBinding
    private lateinit var adapter: LocalItemAdapter
    private val viewModel: StorageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocalAudioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvAudioList.addItemDecoration(addDivider(requireContext()))
            lifecycleScope.launch {
                viewModel.audioList.collectLatest { list ->
                    adapter = LocalItemAdapter(emptyList(), isAudio = true) { position ->
                        EventBus.getDefault().postSticky(AudioData(position, list))
                        startPlayMp3Activity()

                    }
                    rvAudioList.adapter = adapter
                    viewModel.audioList.collect { list ->
                        if (!list.isEmpty()) {
                            adapter.updateItem(list)
                        }
                    }
                }
            }
        }
    }

    private fun startPlayMp3Activity() {
        val intent = Intent(requireContext(), PlayMp3Activity::class.java)
        startActivity(intent)
    }

    companion object {
        fun addDivider(context: Context): DividerItemDecoration {
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            return divider
        }
    }
}