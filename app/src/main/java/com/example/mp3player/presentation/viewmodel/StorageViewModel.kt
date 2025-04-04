package com.example.mp3player.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp3player.domain.model.LocalItem
import com.example.mp3player.domain.usecase.LocalItemUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StorageViewModel(
    private val useCase: LocalItemUseCase
) : ViewModel() {
    val audioList = MutableStateFlow<List<LocalItem>>(emptyList())
    val videos = MutableStateFlow<List<LocalItem>>(emptyList())

    init {
        getAudioList()
        getVideos()
    }

    fun getAudioList() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                useCase.getAudioLocal()
            }
            audioList.value = result
        }
    }

    fun getVideos() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                useCase.getVideosLocal()
            }
            videos.value = result
        }
    }
}