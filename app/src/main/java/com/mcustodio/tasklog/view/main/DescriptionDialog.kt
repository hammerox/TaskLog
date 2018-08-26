package com.mcustodio.tasklog.view.main

import android.app.Activity
import android.view.View
import android.widget.ArrayAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.mcustodio.tasklog.R
import com.mcustodio.tasklog.model.task.Task
import com.mcustodio.tasklog.utils.roundToNearestFiveMinutes
import com.mcustodio.tasklog.utils.toString
import kotlinx.android.synthetic.main.dialog_counter.view.*
import java.util.*

/**
 * Created by logonrm on 07/04/2018.
 */
class DescriptionDialog(activity: Activity, list: List<String>, onSelect: ((String, Date) -> Unit)) {

    val descriptionList = list
    val dialog = MaterialDialog.Builder(activity)
            .title("Atividade")
            .customView(R.layout.dialog_counter, false)
            .positiveText("OK")
            .onPositive { dialog, _ ->
                val description = dialog.view.edit_descdialog_description.text.toString()
                onSelect(description, selectedTime)
            }
            .build()
    val adapter = ArrayAdapter<String>(dialog.context, android.R.layout.simple_list_item_1, descriptionList)
    lateinit var selectedTime: Date



    fun show(task: Task?) {
        selectedTime = task?.startDate ?: Calendar.getInstance().time.roundToNearestFiveMinutes()

        dialog.customView?.edit_descdialog_description?.setText(task?.description)
        dialog.customView?.list_descdialog?.adapter = adapter
        dialog.customView?.list_descdialog?.setOnItemClickListener { parent, view, position, id ->
            dialog.customView?.edit_descdialog_description?.setText(descriptionList[position])
        }
        dialog.customView?.card_descdialog_rem10?.setOnClickListener { updateTime(-10) }
        dialog.customView?.card_descdialog_rem5?.setOnClickListener { updateTime(-5) }
        dialog.customView?.card_descdialog_add5?.setOnClickListener { updateTime(+5) }
        dialog.customView?.card_descdialog_add10?.setOnClickListener { updateTime(+10) }
        dialog.customView?.text_descdialog_time?.text = selectedTime.toString("HH'h'mm")
        dialog.customView?.text_descdialog_timeoriginal?.text = selectedTime.toString("HH'h'mm")
        dialog.show()
    }

    private fun updateTime(minutesToShift: Int) {
        selectedTime.time = selectedTime.time + minutesToShift * 60000
        dialog.customView?.apply {
            text_descdialog_time?.text = selectedTime.toString("HH'h'mm")
            val showOriginalTime = text_descdialog_timeoriginal.text != text_descdialog_time.text
            text_descdialog_timeoriginal.visibility = if (showOriginalTime) View.VISIBLE else View.INVISIBLE
        }
    }

}