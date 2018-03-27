package com.mcustodio.tasklog.repository

import android.content.Context

/**
 * Created by logonrm on 17/02/2018.
 */
abstract class BaseRepository(context: Context) {

    protected val database = AppDatabase.getFrom(context)

}