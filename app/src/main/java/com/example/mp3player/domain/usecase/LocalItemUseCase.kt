package com.example.mp3player.domain.usecase

import com.example.mp3player.domain.model.LocalItem
import com.example.mp3player.domain.repository.ILocalItemRepository

class LocalItemUseCase(private val repository: ILocalItemRepository) {
    fun getAudioLocal(): List<LocalItem> = repository.getAudioList()
    fun getVideosLocal(): List<LocalItem> = repository.getVideos()
}
