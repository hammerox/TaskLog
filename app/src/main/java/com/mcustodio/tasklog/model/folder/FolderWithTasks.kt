package com.mcustodio.tasklog.model.folder

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.mcustodio.tasklog.model.task.Task

/**
 * Created by logonrm on 07/04/2018.
 */
class FolderWithTasks(
        @Embedded
        var folder: Folder? = null,

        @Relation(parentColumn = "id", entityColumn = "folderId")
        var tasks: List<Task>? = null
    ) {
}