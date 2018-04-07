package com.mcustodio.tasklog.model.task

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.mcustodio.tasklog.model.BaseDao
import io.reactivex.Flowable

@Dao
interface TaskDao : BaseDao<Task> {

    @Query("SELECT * FROM Task")
    fun getAll() : LiveData<List<Task>>

    @Query("SELECT * FROM Task")
    fun getAllAsList() : List<Task>

    @Query("DELETE FROM Task")
    fun deleteAll()

}