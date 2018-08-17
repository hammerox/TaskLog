package com.mcustodio.tasklog.view

import android.app.Application
import android.support.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.mcustodio.tasklog.model.AppDatabase

/**
 * Created by logonrm on 17/02/2018.
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        AppDatabase.getFrom(this)
    }
}