package com.example.mp3player.data.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import com.example.mp3player.data.api.DataService
import com.example.mp3player.domain.model.LocalItem
import com.example.mp3player.domain.repository.ILocalAudioRepository
import kotlinx.coroutines.flow.flow

class LocalAudioRepositoryIMPL(private val dataService: DataService) : ILocalAudioRepository {
    @SuppressLint("Recycle")
    override fun getAudioList(): List<LocalItem> {
        var audioList = mutableListOf<LocalItem>()
//        val projection = arrayOf<String>(
//            MediaStore.Audio.Media.DATA,
//            MediaStore.Audio.Media.AUTHOR,
//            MediaStore.Audio.Media.DISPLAY_NAME,
//            MediaStore.Audio.Media._ID
//        )
//        val orderBy = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
//
//        val cursor =
//            resolver.query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                projection,
//                null,
//                null,
//                orderBy
//            )
//
//        if (cursor == null) return listOf()
//        if (cursor.isFirst) {
//            while (cursor.moveToNext()) {
//                val nameIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
//                val dataIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
//                val authorIndex = cursor.getColumnIndex(MediaStore.Audio.Media.AUTHOR)
//                val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
//
//                audioList.add(
//                    LocalItem(
//                        id = cursor.getInt(idIndex),
//                        name = cursor.getString(nameIndex),
//                        author = cursor.getString(authorIndex),
//                        data = cursor.getString(dataIndex)
//                    )
//                )
//            }
//        }
        return audioList
    }
}