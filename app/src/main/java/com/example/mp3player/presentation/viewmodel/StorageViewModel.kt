package com.example.mp3player.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mp3player.domain.usecase.LocalAudioUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StorageViewModel(
    private val useCase: LocalAudioUseCase
) : ViewModel() {
    var number = 0

    init {
        getAudioList()
    }

    fun getAudioList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val audioList = useCase.invoke()
                Log.d("TAG", "getAudioList: $audioList")
            }
        }
    }
}