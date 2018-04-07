package com.mcustodio.tasklog.view.main

import android.app.Application
import android.arch.lifecycle.*
import com.mcustodio.tasklog.model.folder.Folder
import com.mcustodio.tasklog.model.folder.FolderWithTasks
import com.mcustodio.tasklog.model.task.Task
import com.mcustodio.tasklog.repository.folder.FolderRepository
import com.mcustodio.tasklog.repository.task.TaskRepository

class MainViewModel(app: Application) : AndroidViewModel(app) {


    var counter : LiveData<Int>
    var tasks: LiveData<List<Task>>
    var descriptionList: LiveData<List<String>>
    var folders : LiveData<List<FolderWithTasks>>
    var folder : LiveData<FolderWithTasks?>

    private val repository by lazy { TaskRepository(app) }
    private val folderRepo by lazy { FolderRepository(app) }



    init {
        tasks = repository.getDatabase()
        counter = Transformations.map(tasks) { it.size }
        descriptionList = Transformations.map(tasks) { it.mapToDescriptionList() }
        folders = folderRepo.getDatabase()
        folder = Transformations.map(folders) { it?.get(0) }
    }


    fun insert(task: Task) {
        task.folderId = folder.value?.folder?.id
        repository.insert(task)
    }

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


    private fun List<Task>.mapToDescriptionList() : List<String> {
        return this.sortedByDescending { it.startDate }
                .mapNotNull { task -> task.description }
                .distinct()
    }


    fun insertFolder(folder: Folder) {
        folderRepo.insert(folder)
    }

}