package com.mcustodio.tasklog.view.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import com.mcustodio.tasklog.model.task.Task
import com.mcustodio.tasklog.repository.task.TaskRepository

class MainViewModel(app: Application) : AndroidViewModel(app) {


    var counter : LiveData<Int>
    var examples: LiveData<List<Task>>

    private val repository by lazy { TaskRepository(app) }



    init {
        val resistanceFlow = repository.getDatabase()
        counter = LiveDataReactiveStreams.fromPublisher(resistanceFlow.map { it.size })
        examples = LiveDataReactiveStreams.fromPublisher(resistanceFlow)
    }


    fun insert(example: Task) = repository.insert(example)

    fun update(example: Task) = repository.update(example)

    fun delete(example: Task) = repository.delete(example)

    fun clearAll() = repository.clearAll()

}