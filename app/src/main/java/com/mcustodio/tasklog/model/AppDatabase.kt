package com.mcustodio.tasklog.model

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.mcustodio.tasklog.model.folder.Folder
import com.mcustodio.tasklog.model.folder.FolderDao
import com.mcustodio.tasklog.model.task.Task
import com.mcustodio.tasklog.model.task.TaskDao
import com.mcustodio.tasklog.repository.Converter

@Database(entities = arrayOf(Task::class,
        Folder::class),
        version = AppDatabase.VERSION)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun folderDao(): FolderDao

    companion object {
        const val VERSION = 3
        private lateinit var INSTANCE: AppDatabase
        private val databaseName = "baseapp-database"
        private var firstAccess = true


        fun getFrom(context: Context): AppDatabase {
            if (firstAccess) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, databaseName)
                            .addMigrations(MIGRATION_2_3)
                            .build()
                    firstAccess = false
                }
            }
            return INSTANCE
        }


        // Task
        // + isRunningTime
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                val sqlStatement = "ALTER TABLE `Task` ADD COLUMN `isRunningTime` INTEGER"
                database.execSQL(sqlStatement)
            }
        }
    }

}