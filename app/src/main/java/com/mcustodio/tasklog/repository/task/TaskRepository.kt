package com.mcustodio.tasklog.repository.task

import android.arch.lifecycle.LiveData
import android.content.Context
import com.mcustodio.tasklog.model.folder.FolderWithTasks
import com.mcustodio.tasklog.model.task.Task
import com.mcustodio.tasklog.repository.BaseRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class TaskRepository(context: Context) : BaseRepository(context) {


    fun getDatabase() : LiveData<List<Task>> {
        return database.taskDao().getAll()
    }


    fun insert(task: Task) {
        Observable.just(database.taskDao())
                .doOnNext { it.insert(task) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    fun update(task: Task) {
        Observable.just(database.taskDao())
                .doOnNext { it.update(task) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    fun updateAll(tasks: List<Task>) {
        Observable.just(database.taskDao())
                .doOnNext { it.update(tasks) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    fun delete(task: Task) {
        Observable.just(database.taskDao())
                .doOnNext { it.delete(task) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    fun clearAll() {
        Observable.just(database.taskDao())
                .doOnNext { it.deleteAll() }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}