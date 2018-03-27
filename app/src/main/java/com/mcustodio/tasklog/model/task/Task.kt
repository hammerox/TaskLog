package com.mcustodio.tasklog.model.task

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
class Task(@PrimaryKey(autoGenerate = true) var id : Long? = null,
           var startDate: Date? = null,
           var endDate: Date? = null,
           var description: String? = null) {

}