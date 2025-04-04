package com.example.mp3player.domain.repository

import com.example.mp3player.domain.model.LocalItem

interface ILocalItemRepository {
    fun getAudioList(): List<LocalItem>
    fun getVideos(): List<LocalItem>
}