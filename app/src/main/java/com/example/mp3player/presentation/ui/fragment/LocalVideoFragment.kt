package com.example.mp3player.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mp3player.databinding.FragmentLocalVideoBinding

class LocalVideoFragment : Fragment() {
    private lateinit var binding: FragmentLocalVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocalVideoBinding.inflate(inflater, container, false)
        return binding.root
    }
}