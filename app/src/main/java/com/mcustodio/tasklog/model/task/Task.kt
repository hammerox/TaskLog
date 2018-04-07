package com.mcustodio.tasklog.model.task

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.mcustodio.tasklog.model.folder.Folder
import java.util.*

@Entity(foreignKeys = arrayOf(ForeignKey(entity = Folder::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("folderId"),
        onDelete = ForeignKey.CASCADE)))
class Task(@PrimaryKey(autoGenerate = true) var id : Long? = null,
           var startDate: Date? = null,
           var endDate: Date? = null,
           var description: String? = null,
           var folderId: Long? = null) {

}