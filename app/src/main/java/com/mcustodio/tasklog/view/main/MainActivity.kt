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
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import android.text.InputType
import com.mcustodio.tasklog.model.folder.Folder
import com.mcustodio.tasklog.utils.TimeDiff
import com.mcustodio.tasklog.utils.ignoreSeconds


class MainActivity : AppCompatActivity() {


    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
    private val counterAdapter = TaskAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setButtonClickListener()
        setRecyclerView()
        observeTaskList()
        observeDescriptionList()
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
        counterAdapter.onDiffTimeClick = onDiffTimeItemClick
    }


    private fun observeTaskList() {
        viewModel.tasks.observe(this, Observer {
            it?.let { counterAdapter.data = it }
        })
    }


    private fun observeDescriptionList() {
        viewModel.descriptionList.observe(this, Observer {
            // Apenas observar para popular o LiveData
        })

        viewModel.folder.observe(this, Observer {
            if (it == null) {
                val folder = Folder()
                folder.name = "TESTE"
                folder.createdDate = Calendar.getInstance().time
                viewModel.insertFolder(folder)
            }
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
            newTask.startDate = Calendar.getInstance().time.ignoreSeconds()
            newTask.description = description?.trim()
            viewModel.insert(newTask)
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
