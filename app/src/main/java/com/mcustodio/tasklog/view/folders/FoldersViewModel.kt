package com.mcustodio.tasklog.view.folders

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.mcustodio.tasklog.model.folder.Folder
import com.mcustodio.tasklog.repository.folder.FolderRepository
import java.util.*

class FoldersViewModel(app: Application) : AndroidViewModel(app) {

    private val folderRepo = FolderRepository(app)

    val folders: LiveData<List<Folder>>

    init {
        folders = folderRepo.getDatabase()
    }


    fun createFolder(name: String) {
        val newFolder = Folder()
        newFolder.name = name
        newFolder.createdDate = Calendar.getInstance().time
        folderRepo.insert(newFolder)
    }


    fun delete(folder: Folder) {
        folderRepo.delete(folder)
    }
}