package com.mcustodio.tasklog.model.folder

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by logonrm on 07/04/2018.
 */
@Entity
class Folder(@PrimaryKey(autoGenerate = true) var id : Long? = null,
             var name: String? = null,
             var createdDate: Date? = null) {

}