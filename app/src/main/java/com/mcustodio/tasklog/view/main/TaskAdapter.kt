package com.mcustodio.tasklog.view.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mcustodio.tasklog.R
import com.mcustodio.tasklog.model.task.Task



class TaskAdapter : RecyclerView.Adapter<TaskViewHolder>() {


    var onItemClick: ((Task) -> Unit)? = null
    var onItemLongClick: ((Task) -> Unit)? = null
    var onTimeClick: ((Task) -> Unit)? = null
    var onDiffTimeClick: ((Pair<Task, Task>) -> Unit)? = null

    var data : List<Task> = listOf()
        set(value) {
            field = value.sortedBy { it.startDate }
            notifyDataSetChanged()
        }



    override fun getItemCount(): Int {
        return data.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }


    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = data[position]
        val next = position + 1
        val nextTask = if (next < data.size) data[next] else null
        holder.setView(task, nextTask)
        holder.setClickListener(onItemClick, task)
        holder.setLongClickListener(onItemLongClick, task)
        holder.setTimeClickListener(onTimeClick, task)
        holder.setDiffTimeClickListener(onDiffTimeClick, task, nextTask)
    }

}