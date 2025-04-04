package com.example.mp3player.domain.model

data class LocalItem(
    val id: Int,
    val name: String,
    val author: String,
    val avatar: String? = "",
    val data: String,
)