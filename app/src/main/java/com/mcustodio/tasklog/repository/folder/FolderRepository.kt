package com.mcustodio.tasklog.repository.folder

import android.arch.lifecycle.LiveData
import android.content.Context
import com.mcustodio.tasklog.model.folder.Folder
import com.mcustodio.tasklog.model.folder.FolderWithTasks
import com.mcustodio.tasklog.repository.BaseRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Created by logonrm on 07/04/2018.
 */
class FolderRepository(context: Context) : BaseRepository(context) {


    fun getDatabaseWithTasks() : LiveData<List<FolderWithTasks>> {
        return database.folderDao().getAllWithTasks()
    }


    fun getDatabase() : LiveData<List<Folder>> {
        return database.folderDao().getAll()
    }


    fun insert(folder: Folder) {
        Observable.just(database.folderDao())
                .doOnNext { it.insert(folder) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    fun delete(folder: Folder) {
        Observable.just(database.folderDao())
                .doOnNext { it.delete(folder) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

}