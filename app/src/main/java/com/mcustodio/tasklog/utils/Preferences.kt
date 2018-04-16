package com.mcustodio.tasklog.utils

import android.content.Context
import android.content.SharedPreferences


class Preferences(context: Context) {

    val PREFS_FILENAME = "com.mcustodio.tasklog"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)


    private val SELECTED_FOLDER_ID = "folder_id"
    var lastSelectedFolder: Long
        get() = prefs.getLong(SELECTED_FOLDER_ID, -1)
        set(value) = prefs.edit().putLong(SELECTED_FOLDER_ID, value).apply()


}