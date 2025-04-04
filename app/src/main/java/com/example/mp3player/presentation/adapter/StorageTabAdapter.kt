package com.example.mp3player.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class StorageTabAdapter(
    private val fragmentManager: FragmentManager,
    private val lifecycle: Lifecycle,
    private val listFragment: List<Fragment>,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment = listFragment[position]
    override fun getItemCount(): Int = listFragment.size
}