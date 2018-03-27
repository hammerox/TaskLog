package com.mcustodio.tasklog.utils

import android.content.Context
import android.content.SharedPreferences


class Preferences(context: Context) {

    val PREFS_FILENAME = "com.hammerox.beattheresistance"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)


//    private val SELECTED_ROUTE_ID = "ROUTE"
//    var lastRouteId: String?
//        get() = prefs.getString(SELECTED_ROUTE_ID, null)
//        set(value) = prefs.edit().putString(SELECTED_ROUTE_ID, value).apply()


}