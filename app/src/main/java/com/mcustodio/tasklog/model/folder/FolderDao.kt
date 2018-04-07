package com.mcustodio.tasklog.model.folder

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import com.mcustodio.tasklog.model.BaseDao

@Dao
interface FolderDao : BaseDao<Folder> {

    @Query("SELECT * FROM Folder")
    fun getAll() : LiveData<List<Folder>>

    @Query("SELECT * FROM Folder")
    fun getAllAsList() : List<Folder>

    @Query("DELETE FROM Folder")
    fun deleteAll()

    @Transaction
    @Query("SELECT * FROM Folder")
    fun getAllWithTasks() : LiveData<List<FolderWithTasks>>

}