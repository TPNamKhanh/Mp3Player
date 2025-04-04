package com.example.mp3player.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mp3player.databinding.FragmentLocalVideoBinding
import com.example.mp3player.presentation.adapter.LocalItemAdapter
import com.example.mp3player.presentation.viewmodel.StorageViewModel
import kotlinx.coroutines.launch
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
            adapter = LocalItemAdapter(emptyList())
            rvVideoList.adapter = adapter
            lifecycleScope.launch {
                viewModel.videos.collect { list ->
                    if (!list.isEmpty()) {
                        adapter.updateItem(list)
                    }
                }
            }
        }
    }
}