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


    fun insert(example: Task) {
        Observable.just(database.resistanceDao())
                .doOnNext { it.insert(example) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    fun update(example: Task) {
        Observable.just(database.resistanceDao())
                .doOnNext { it.update(example) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    fun delete(example: Task) {
        Observable.just(database.resistanceDao())
                .doOnNext { it.delete(example) }
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