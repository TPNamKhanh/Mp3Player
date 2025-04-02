package com.example.mp3player.domain.usecase

import com.example.mp3player.domain.model.LocalItem
import com.example.mp3player.domain.repository.ILocalAudioRepository

class LocalAudioUseCase(private val repository: ILocalAudioRepository) {
    operator fun invoke(): List<LocalItem> = repository.getAudioList()
}
