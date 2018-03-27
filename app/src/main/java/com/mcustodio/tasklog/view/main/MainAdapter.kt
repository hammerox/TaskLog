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



class MainAdapter : RecyclerView.Adapter<MainAdapter.CounterViewHolder>() {

    var onItemClick: ((Task) -> Unit)? = null
    var onItemLongClick: ((Task) -> Unit)? = null

    var data : List<Task> = listOf()
        set(value) {
            field = value.sortedByDescending { it.startDate }
            notifyDataSetChanged()
        }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_counter, parent, false)
        return CounterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CounterViewHolder, position: Int) {
        val task = data[position]
        val next = if (position - 1 >= 0) data[position - 1] else null
        holder.setView(task, next)
        holder.setClickListener(onItemClick, task)
        holder.setLongClickListener(onItemLongClick, task)
    }



    class CounterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val card = view.card_counteritem
        private val date = view.text_counteritem_date
        private val description = view.text_counteritem_description
        private val diff = view.text_counteritem_diff

        fun setView(task: Task, nextTask: Task?) {
            date.text = task.startDate?.toString("HH'h'mm") ?: ""
            description.text = task.description
            description.switchVisibility(!task.description.isNullOrBlank())

            diff.switchVisibility(nextTask != null)
            diff.text = nextTask?.let {
                val first = nextTask.startDate?.time ?: 0
                val last = task.startDate?.time ?: 0
                val timeDifference = first - last
                (timeDifference / 60000).toString() + "m"
            } ?: ""
        }

        fun setClickListener(onItemClick: ((Task) -> Unit)?, example: Task) {
            card.setOnClickListener { onItemClick?.invoke(example) }
        }

        fun setLongClickListener(onItemLongClick: ((Task) -> Unit)?, example: Task) {
            card.setOnLongClickListener {
                onItemLongClick?.invoke(example)
                true
            }
        }
    }
}