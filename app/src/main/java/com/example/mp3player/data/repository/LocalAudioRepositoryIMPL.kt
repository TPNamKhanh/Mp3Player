package com.example.mp3player.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.MediaStore.Audio.Media
import android.util.Log
import com.example.mp3player.domain.model.LocalItem
import com.example.mp3player.domain.repository.ILocalAudioRepository

class LocalAudioRepositoryIMPL(private val resolver: ContentResolver) : ILocalAudioRepository {
    @SuppressLint("Recycle")
    override fun getAudioList(): List<LocalItem> {
        var audioList = mutableListOf<LocalItem>()
        val projection = arrayOf<String>(
            Media.DATA,
            Media.AUTHOR,
            Media.DISPLAY_NAME,
            Media._ID
        )
        val orderBy = "${Media.DISPLAY_NAME} ASC"

        val cursor =
            resolver.query(
                Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                orderBy
            ) ?: return listOf()

        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                val nameIndex = cursor.getColumnIndex(Media.DISPLAY_NAME)
                val dataIndex = cursor.getColumnIndex(Media.DATA)
                val authorIndex = cursor.getColumnIndex(Media.AUTHOR)
                val idIndex = cursor.getColumnIndex(Media._ID)
                val localItem = LocalItem(
                    id = cursor.getInt(idIndex),
                    name = cursor.getString(nameIndex),
                    author = cursor.getString(authorIndex) ?: "Unknown",
                    data = cursor.getString(dataIndex)
                )
                audioList.add(localItem)
            }
        }
        return audioList
    }
}