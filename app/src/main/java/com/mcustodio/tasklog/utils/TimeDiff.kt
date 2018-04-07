package com.mcustodio.tasklog.utils

import com.mcustodio.tasklog.model.task.Task

class TimeDiff {

    companion object {

        fun minuteDiff(task: Task, nextTask: Task) : Long {
            val first = nextTask.startDate?.time ?: 0
            val last = task.startDate?.time ?: 0
            val timeDifference = first - last
            return Math.round(timeDifference / 60000.0)
        }

    }
}