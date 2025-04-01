package com.example.mp3player.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mp3player.databinding.StorageFragmentBinding

class StorageFragment : Fragment() {
    private lateinit var binding: StorageFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StorageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}