package com.mcustodio.tasklog.repository

import android.content.Context
import com.mcustodio.tasklog.model.AppDatabase

/**
 * Created by logonrm on 17/02/2018.
 */
abstract class BaseRepository(context: Context) {

    protected val database = AppDatabase.getFrom(context)

}