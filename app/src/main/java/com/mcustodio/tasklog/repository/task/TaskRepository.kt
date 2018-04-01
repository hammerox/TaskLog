package com.mcustodio.tasklog.repository.task

import android.content.Context
import com.mcustodio.tasklog.model.task.Task
import com.mcustodio.tasklog.repository.BaseRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class TaskRepository(context: Context) : BaseRepository(context) {


    fun getDatabase() : Flowable<List<Task>> {
        return Flowable.just(database.resistanceDao())
                .flatMap { it.getAll() }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
    }


    fun insert(task: Task) {
        Observable.just(database.resistanceDao())
                .doOnNext { it.insert(task) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    fun update(task: Task) {
        Observable.just(database.resistanceDao())
                .doOnNext { it.update(task) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    fun updateAll(tasks: List<Task>) {
        Observable.just(database.resistanceDao())
                .doOnNext { it.update(tasks) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    fun delete(task: Task) {
        Observable.just(database.resistanceDao())
                .doOnNext { it.delete(task) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    fun clearAll() {
        Observable.just(database.resistanceDao())
                .doOnNext { it.deleteAll() }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}