package com.mcustodio.tasklog.view.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mcustodio.tasklog.R
import com.mcustodio.tasklog.model.task.Task
import com.mcustodio.tasklog.utils.switchVisibility
import com.mcustodio.tasklog.utils.toString
import kotlinx.android.synthetic.main.item_counter.view.*



class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var onItemClick: ((Task) -> Unit)? = null
    var onItemLongClick: ((Task) -> Unit)? = null
    var onTimeClick: ((Task) -> Unit)? = null

    var data : List<Task> = listOf()
        set(value) {
            field = value.sortedByDescending { it.startDate }
            notifyDataSetChanged()
        }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_counter, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = data[position]
        val next = if (position - 1 >= 0) data[position - 1] else null
        holder.setView(task, next)
        holder.setClickListener(onItemClick, task)
        holder.setLongClickListener(onItemLongClick, task)
        holder.setTimeClickListener(onTimeClick, task)
    }



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


        private fun timeDifference(task: Task, nextTask: Task?) : String {
            return nextTask?.let {
                val first = nextTask.startDate?.time ?: 0
                val last = task.startDate?.time ?: 0
                val timeDifference = first - last
                val minutesDiff = (timeDifference / 60000)
                val hoursCount = (minutesDiff / 60)
                val minuteCount = (minutesDiff % 60)
                return if (hoursCount >= 1) "${hoursCount}h ${minuteCount}m"
                else "${minuteCount}m"
            } ?: ""
        }
    }
}