package com.example.mp3player.utils

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration

fun isDarkModeEnabled(context: Context): Boolean {
//    val currentMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
//    val isDarMode = currentMode == Configuration.UI_MODE_NIGHT_YES
    val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    return uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
}