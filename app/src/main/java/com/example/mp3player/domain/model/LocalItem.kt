package com.example.mp3player.domain.model

import java.io.Serializable

data class LocalItem(
    var id: Int,
    var name: String,
    var author: String,
    var avatar: String? = "",
    var data: String,
    var duration: Int,
    var isPlaying: Boolean = false,
) : Serializable