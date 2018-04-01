package com.mcustodio.tasklog.view.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import com.mcustodio.tasklog.model.task.Task
import com.mcustodio.tasklog.repository.task.TaskRepository

class MainViewModel(app: Application) : AndroidViewModel(app) {


    var counter : LiveData<Int>
    var tasks: LiveData<List<Task>>

    private val repository by lazy { TaskRepository(app) }



    init {
        val resistanceFlow = repository.getDatabase()
        counter = LiveDataReactiveStreams.fromPublisher(resistanceFlow.map { it.size })
        tasks = LiveDataReactiveStreams.fromPublisher(resistanceFlow)
    }


    fun insert(task: Task) = repository.insert(task)

    fun update(task: Task) = repository.update(task)

    fun delete(task: Task) = repository.delete(task)

    fun clearAll() = repository.clearAll()


    fun shiftTime(task: Task, minutesToShift: Int) {
        val shiftedTasks = shiftInto(task, minutesToShift)
        shiftedTasks?.let { repository.updateAll(it) }
    }


    private fun shiftInto(task: Task, minutesToShift: Int) : List<Task>? {
        return tasks.value
                ?.filter { t -> t.startDate?.time ?: 0 >= task.startDate?.time ?: 0 }
                ?.onEach { t -> t.startDate?.time = t.startDate!!.time - minutesToShift * 60000 }
    }

}