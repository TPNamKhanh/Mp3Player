package com.example.mp3player.presentation.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.mp3player.databinding.FragmentLocalAudioBinding
import com.example.mp3player.presentation.adapter.LocalItemAdapter
import com.example.mp3player.presentation.viewmodel.StorageViewModel
import kotlinx.coroutines.launch
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvAudioList.addItemDecoration(addDivider(requireContext()))
            adapter = LocalItemAdapter(emptyList(), isAudio = true)
            rvAudioList.adapter = adapter
            lifecycleScope.launch {
                viewModel.audioList.collect { list ->
                    if (!list.isEmpty()) {
                        adapter.updateItem(list)
                    }
                }
            }
        }
    }

    companion object {
        fun addDivider(context: Context): DividerItemDecoration {
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            return divider
        }
    }
}