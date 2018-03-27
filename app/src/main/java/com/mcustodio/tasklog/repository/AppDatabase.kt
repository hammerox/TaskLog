package com.mcustodio.tasklog.repository

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.mcustodio.tasklog.model.task.Task
import com.mcustodio.tasklog.model.task.TaskDao

@Database(entities = arrayOf(Task::class), version = AppDatabase.VERSION)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun resistanceDao(): TaskDao

    companion object {
        const val VERSION = 1
        private lateinit var INSTANCE: AppDatabase
        private val databaseName = "baseapp-database"
        private var firstAccess = true


        fun getFrom(context: Context): AppDatabase {
            if (firstAccess) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, databaseName)
                            .fallbackToDestructiveMigration()   // REMOVE THIS TO USE VERSIONS
                            .build()
                    firstAccess = false
                }
            }
            return INSTANCE
        }
    }

}