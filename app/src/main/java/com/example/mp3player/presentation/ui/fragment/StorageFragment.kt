package com.example.mp3player.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.mp3player.databinding.StorageFragmentBinding
import com.example.mp3player.presentation.adapter.StorageTabAdapter
import com.example.mp3player.presentation.viewmodel.StorageViewModel
import com.google.android.material.tabs.TabLayoutMediator

class StorageFragment : Fragment() {
    private lateinit var binding: StorageFragmentBinding
    private val viewModel: StorageViewModel by viewModel()
    private val fragmentList = listOf<Fragment>(LocalAudioFragment(), LocalVideoFragment())
    private val tabList = listOf<String>("Audio", "Video")
    private val storageAdapter by lazy {
        StorageTabAdapter(
            childFragmentManager,
            lifecycle,
            fragmentList
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StorageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            vpStorageData.apply {
                adapter = storageAdapter
            }
            TabLayoutMediator(tlStorage, vpStorageData) { tab, position ->
                tab.text = tabList[position]
            }.attach()
            viewModel.number
        }
    }
}