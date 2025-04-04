package com.example.mp3player.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp3player.domain.model.LocalItem
import com.example.mp3player.domain.usecase.LocalAudioUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StorageViewModel(
    private val useCase: LocalAudioUseCase
) : ViewModel() {
    val audioList = MutableStateFlow<List<LocalItem>>(emptyList())

    init {
        getAudioList()
    }

    fun getAudioList() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                useCase.invoke()
            }
            audioList.value = result
        }
    }
}