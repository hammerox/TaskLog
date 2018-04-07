package com.mcustodio.tasklog.view.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.Observer
import android.util.Log
import com.mcustodio.tasklog.model.task.Task
import com.mcustodio.tasklog.repository.task.TaskRepository
import io.reactivex.Flowable

class MainViewModel(app: Application) : AndroidViewModel(app) {


    var counter : LiveData<Int>
    var tasks: LiveData<List<Task>>
    var descriptionList: LiveData<List<String>>

    private val repository by lazy { TaskRepository(app) }



    init {
        val taskFlow = repository.getDatabase()
        counter = LiveDataReactiveStreams.fromPublisher(taskFlow.map { it.size })
        tasks = LiveDataReactiveStreams.fromPublisher(taskFlow)
        descriptionList = LiveDataReactiveStreams.fromPublisher(taskFlow.mapToDescriptionList())
    }


    fun insert(task: Task) = repository.insert(task)

    fun update(task: Task) = repository.update(task)

    fun delete(task: Task) = repository.delete(task)

    fun clearAll() = repository.clearAll()


    fun shiftTime(task: Task, minutesToShift: Int) {
        val shiftedTasks = shiftStartDate(task, minutesToShift)
        shiftedTasks?.let { repository.updateAll(it) }
    }


    private fun shiftStartDate(task: Task, minutesToShift: Int) : List<Task>? {
        return tasks.value
                ?.filter { t -> t.startDate?.time ?: 0 >= task.startDate?.time ?: 0 }
                ?.onEach { t -> t.startDate?.time = t.startDate!!.time - minutesToShift * 60000 }
    }


    private fun Flowable<List<Task>>.mapToDescriptionList() : Flowable<List<String>> {
        return this.map { list ->
            list.sortedByDescending { it.startDate }
                .mapNotNull { task -> task.description }
                .distinct()
        }
    }

}