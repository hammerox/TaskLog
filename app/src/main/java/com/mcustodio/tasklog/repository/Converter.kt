package com.mcustodio.tasklog.repository

import android.arch.persistence.room.TypeConverter
import java.util.*


class Converter {

    val DELIMITER = "/*/"

    @TypeConverter
    fun toDate(value: Long?): Date? = value?.let { Date(value) }

    @TypeConverter
    fun toLong(value: Date?): Long? = value?.time

    @TypeConverter
    fun arrayToString(value: Array<String>?) : String? = value?.joinToString(DELIMITER)

    @TypeConverter
    fun stringToArray(value: String?) : Array<String>? = value?.split(DELIMITER)?.toTypedArray()

}