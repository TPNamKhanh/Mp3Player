package com.example.mp3player.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.MediaStore.Audio
import android.provider.MediaStore.Video
import com.example.mp3player.domain.model.LocalItem
import com.example.mp3player.domain.repository.ILocalItemRepository

class LocalItemRepositoryIMPL(private val resolver: ContentResolver) : ILocalItemRepository {
    @SuppressLint("Recycle")
    override fun getAudioList(): List<LocalItem> {
        var audioList = mutableListOf<LocalItem>()
        val projection = arrayOf<String>(
            Audio.Media.DATA,
//            Audio.Media.AUTHOR,
            Audio.Media.DISPLAY_NAME,
            Audio.Media._ID,
            Audio.Media.DURATION
        )
        val orderBy = "${Audio.Media.DISPLAY_NAME} ASC"

        val cursor =
            resolver.query(
                Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                orderBy
            ) ?: return listOf()

        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                val nameIndex = cursor.getColumnIndex(Audio.Media.DISPLAY_NAME)
                val dataIndex = cursor.getColumnIndex(Audio.Media.DATA)
//                val authorIndex = cursor.getColumnIndex(Audio.Media.AUTHOR)
                val idIndex = cursor.getColumnIndex(Audio.Media._ID)
                val durationIndex = cursor.getColumnIndex(Audio.Media.DURATION)
                val localItem = LocalItem(
                    id = cursor.getInt(idIndex),
                    name = cursor.getString(nameIndex),
                    author = "Unknown",
                    data = cursor.getString(dataIndex),
                    duration = cursor.getInt(durationIndex)
                )
                audioList.add(localItem)
            }
        }
        return audioList
    }

    override fun getVideos(): List<LocalItem> {
        var videos = mutableListOf<LocalItem>()
        val projection = arrayOf<String>(
            Video.Media.DATA,
//            Video.Media.AUTHOR,
            Video.Media.DISPLAY_NAME,
            Video.Media._ID,
            Video.Media.DURATION,
        )
        val orderBy = "${Video.Media.DISPLAY_NAME} ASC"

        val cursor =
            resolver.query(
                Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                orderBy
            ) ?: return listOf()
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                val nameIndex = cursor.getColumnIndex(Video.Media.DISPLAY_NAME)
                val dataIndex = cursor.getColumnIndex(Video.Media.DATA)
//                val authorIndex = cursor.getColumnIndex(Video.Media.AUTHOR)
                val idIndex = cursor.getColumnIndex(Video.Media._ID)
                val durationIndex =  cursor.getColumnIndex(Video.Media.DURATION)
                val localItem = LocalItem(
                    id = cursor.getInt(idIndex),
                    name = cursor.getString(nameIndex),
                    author =  "Unknown",
                    data = cursor.getString(dataIndex),
                    duration = cursor.getInt(durationIndex)
                )
                videos.add(localItem)
            }
        }
        return videos
    }
}