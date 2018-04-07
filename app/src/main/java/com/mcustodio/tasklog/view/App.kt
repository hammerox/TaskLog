package com.mcustodio.tasklog.view

import android.app.Application
import com.facebook.stetho.Stetho
import com.mcustodio.tasklog.repository.AppDatabase

/**
 * Created by logonrm on 17/02/2018.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        AppDatabase.getFrom(this)
    }
}