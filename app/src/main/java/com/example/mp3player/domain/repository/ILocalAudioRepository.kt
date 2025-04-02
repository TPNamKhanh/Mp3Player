package com.example.mp3player.domain.repository

import com.example.mp3player.domain.model.LocalItem

interface ILocalAudioRepository {
    fun getAudioList(): List<LocalItem>
}