package com.mcustodio.tasklog.view.main

import android.support.v7.widget.RecyclerView
import android.view.View
import com.mcustodio.tasklog.model.task.Task
import com.mcustodio.tasklog.utils.TimeDiff
import com.mcustodio.tasklog.utils.switchVisibility
import com.mcustodio.tasklog.utils.toString
import kotlinx.android.synthetic.main.item_task.view.*

/**
 * Created by logonrm on 16/04/2018.
 */
class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val card = view.card_counteritem
    private val date = view.text_counteritem_date
    private val description = view.text_counteritem_description
    private val diff = view.text_counteritem_diff


    fun setView(task: Task, nextTask: Task?) {
        date.text = task.startDate?.toString("HH'h'mm") ?: ""
        description.text = task.description
        description.switchVisibility(!task.description.isNullOrBlank())

        diff.switchVisibility(nextTask != null)
        diff.text = timeDifference(task, nextTask)
    }


    fun setClickListener(onItemClick: ((Task) -> Unit)?, task: Task) {
        card.setOnClickListener { onItemClick?.invoke(task) }
    }


    fun setLongClickListener(onItemLongClick: ((Task) -> Unit)?, task: Task) {
        card.setOnLongClickListener {
            onItemLongClick?.invoke(task)
            true
        }
    }


    fun setTimeClickListener(onTimeClick: ((Task) -> Unit)?, task: Task) {
        date.setOnClickListener { onTimeClick?.invoke(task) }
    }


    fun setDiffTimeClickListener(onDiffTimeClick: ((Pair<Task,Task>) -> Unit)?, task: Task, next: Task?) {
        if (next != null) diff.setOnClickListener { onDiffTimeClick?.invoke(task to next) }
    }


    private fun timeDifference(task: Task, nextTask: Task?) : String {
        return nextTask?.let {
            val minutesDiff = TimeDiff.minuteDiff(task, nextTask)
            val hoursCount = (minutesDiff / 60)
            val minuteCount = (minutesDiff % 60)
            return if (hoursCount >= 1) "${hoursCount}h ${minuteCount}m"
            else "${minuteCount}m"
        } ?: ""
    }
}