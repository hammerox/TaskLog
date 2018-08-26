package com.mcustodio.tasklog.view.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
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
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import android.text.InputType
import com.mcustodio.tasklog.utils.Preferences
import com.mcustodio.tasklog.utils.TimeDiff
import com.mcustodio.tasklog.utils.roundToNearestFiveMinutes


class MainActivity : AppCompatActivity() {


    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
    private val taskAdapter = TaskAdapter()
    private val folderId by lazy { intent.getLongExtra(KEY_FOLDERID, -1) }
    private var scrollToBottom = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setButtonClickListener()
        setRecyclerView()
        observeVariables()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_counter, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> exitActivity()
            R.id.menuitem_counter_delete -> viewModel.clearAll()
        }
        return true
    }


    override fun onBackPressed() {
        super.onBackPressed()
        exitActivity()
    }

    private fun setButtonClickListener() {
        fab_task_add.setOnClickListener {
            askForDescription()
        }
        fab_task_stop.setOnClickListener {
            createTimeBreakTask()
        }
    }


    private fun setRecyclerView() {
        list_task.layoutManager = LinearLayoutManager(this)
        list_task.adapter = taskAdapter
        ViewCompat.setNestedScrollingEnabled(list_task, false)
        taskAdapter.onItemClick = onCardItemClick
        taskAdapter.onItemLongClick = onCardItemLongClick
        taskAdapter.onTimeClick = onTimeItemClick
        taskAdapter.onDiffTimeClick = onDiffTimeItemClick
    }


    private fun observeVariables() {
        viewModel.loadData(folderId)
        observeFolder()
        observeTaskList()
        observeDescriptionList()
    }


    private fun observeFolder() {
        viewModel.folder.observe(this, Observer {
            Preferences(this).lastSelectedFolder = it?.id ?: -1
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = it?.name
        })
    }


    private fun observeTaskList() {
        viewModel.tasks.observe(this, Observer {
            it?.let { tasks ->
                taskAdapter.data = tasks
                val showTimeBreakFab = tasks.sortedBy { it.startDate }.lastOrNull()?.isActive() ?: false
                if (showTimeBreakFab) fab_task_stop.show() else fab_task_stop.hide()
                if (scrollToBottom) {
                    list_task.scrollToPosition(tasks.size - 1)
                    scrollToBottom = false
                }
            }
        })
    }


    private fun observeDescriptionList() {
        viewModel.descriptionList.observe(this, Observer {
            // Apenas observar para popular o LiveData
        })
    }


    private fun exitActivity() {
        Preferences(this).lastSelectedFolder = -1   // Clearing variable
        finish()
    }


    private val onCardItemClick : ((Task) -> Unit) = { task ->
        if (task.isActive()) askForDescription(task)
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


    private val onDiffTimeItemClick: ((Pair<Task, Task>) -> Unit) = { tasks ->
        val minuteDiff = TimeDiff.minuteDiff(tasks.first, tasks.second)

        MaterialDialog.Builder(this)
                .title("Duração em minutos")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("", minuteDiff.toString(), { dialog, input ->
                    val inputInt = input.toString().toInt()
                    val minutesToShift = minuteDiff.toInt() - inputInt
                    viewModel.shiftTime(tasks.second, minutesToShift)
                }).show()
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
    private fun askForDescription(task: Task? = null) {
        val list = viewModel.descriptionList.value ?: listOf()
        val onSelect: ((String) -> Unit) = { description ->
            createOrUpdateTask(task, description)
        }
        DescriptionDialog(this, list, onSelect).show(task)
    }


    private fun createOrUpdateTask(task: Task?, description: String?) {
        if (task != null) {
            task.description = description
            viewModel.update(task)
        } else {
            val newTask = Task()
            newTask.startDate = Calendar.getInstance().time.roundToNearestFiveMinutes()
            newTask.description = description?.trim()
            newTask.isRunningTime = true
            viewModel.insert(newTask)
        }
    }


    private fun createTimeBreakTask() {
        val newTask = Task()
        newTask.isRunningTime = false
        newTask.startDate = Calendar.getInstance().time.roundToNearestFiveMinutes()
        viewModel.insert(newTask)
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



    companion object {
        const val KEY_FOLDERID = "folder_id"

        fun launch(from: Context, folderId: Long) {
            val intent = Intent(from, MainActivity::class.java)
            intent.putExtra(KEY_FOLDERID, folderId)
            from.startActivity(intent)
        }
    }

}
