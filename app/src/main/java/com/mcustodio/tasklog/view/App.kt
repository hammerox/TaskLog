package com.mcustodio.tasklog.view

import android.app.Application
import com.mcustodio.tasklog.repository.AppDatabase

/**
 * Created by logonrm on 17/02/2018.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.getFrom(this)
    }
}