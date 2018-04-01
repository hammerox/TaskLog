package com.mcustodio.tasklog.view.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.mcustodio.tasklog.R
import com.mcustodio.tasklog.model.task.Task
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.afollestad.materialdialogs.MaterialDialog
import com.mcustodio.tasklog.utils.toast
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.dialog_counter.view.*


class MainActivity : AppCompatActivity() {


    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
    private val counterAdapter = TaskAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setButtonClickListener()
        setRecyclerView()
//        observeCounter()
        observeResistanceList()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_counter, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuitem_counter_delete -> viewModel.clearAll()
        }
        return true
    }


    private fun setButtonClickListener() {
        fab_counter_add.setOnClickListener {
            askForDescription()
        }
    }


    private fun setRecyclerView() {
        list_counter.layoutManager = LinearLayoutManager(this)
        list_counter.adapter = counterAdapter
        ViewCompat.setNestedScrollingEnabled(list_counter, false)
        counterAdapter.onItemClick = onCardItemClick
        counterAdapter.onItemLongClick = onCardItemLongClick
        counterAdapter.onTimeClick = onTimeItemClick
    }


    private fun observeCounter() {
        viewModel.counter.observe(this, Observer {
            toast(it?.toString() ?: "")
        })
    }


    private fun observeResistanceList() {
        viewModel.tasks.observe(this, Observer {
            it?.let { counterAdapter.data = it }
        })
    }


    private val onCardItemClick : ((Task) -> Unit) = { task ->
        askForDescription(task)

    }


    private val onTimeItemClick : ((Task) -> Unit) = { task ->
        val now = Calendar.getInstance()
        now.time = task.startDate
        val tdp = TimePickerDialog.newInstance(
                onTimeSet(task),
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        )
        tdp.show(fragmentManager, "TimePickerDialog")
    }


    private val onCardItemLongClick : ((Task) -> Unit) = { example ->
        val options = listOf("Deletar")
        MaterialDialog.Builder(this)
                .items(options)
                .itemsCallback { _, _, position, _ ->
                    when (position) {
                        0 -> viewModel.delete(example)
                    }
                }
                .show()
    }


    // If example == null -> insert
    // If example != null -> update
    private fun askForDescription(example: Task? = null) {
        val dialog = MaterialDialog.Builder(this)
                .title("Descreva")
                .customView(R.layout.dialog_counter, false)
                .positiveText("OK")
                .onPositive { dialog, _ ->
                    val description = dialog.view.edit_counterdialog_description.text.toString()
                    createOrUpdateResistance(example, description)
                }
                .build()
        dialog.customView?.edit_counterdialog_description?.setText(example?.description)
        dialog.show()
    }


    private fun createOrUpdateResistance(example: Task?, description: String?) {
        if (example != null) {
            example.description = description
            viewModel.update(example)
        } else {
            val newResistance = Task()
            newResistance.startDate = Calendar.getInstance().time
            newResistance.description = description
            viewModel.insert(newResistance)
        }
    }


    private fun onTimeSet(task: Task) : TimePickerDialog.OnTimeSetListener {
        return TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, second ->
            val now = Calendar.getInstance()
            now.time = task.startDate
            now.set(Calendar.HOUR_OF_DAY, hourOfDay)
            now.set(Calendar.MINUTE, minute)
            now.set(Calendar.SECOND, second)
            task.startDate = now.time
            viewModel.update(task)
        }
    }

}
