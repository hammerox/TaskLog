package com.mcustodio.tasklog.view.main

import android.app.Activity
import android.widget.ArrayAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.mcustodio.tasklog.R
import com.mcustodio.tasklog.model.task.Task
import kotlinx.android.synthetic.main.dialog_counter.view.*

/**
 * Created by logonrm on 07/04/2018.
 */
class DescriptionDialog(activity: Activity, list: List<String>, onSelect: ((String) -> Unit)) {

    val descriptionList = list
    val dialog = MaterialDialog.Builder(activity)
            .title("Atividade")
            .customView(R.layout.dialog_counter, false)
            .positiveText("OK")
            .onPositive { dialog, _ ->
                val description = dialog.view.edit_descdialog_description.text.toString()
                onSelect(description)
            }
            .build()
    val adapter = ArrayAdapter<String>(dialog.context, android.R.layout.simple_list_item_1, descriptionList)



    fun show(task: Task?) {
        dialog.customView?.edit_descdialog_description?.setText(task?.description)
        dialog.customView?.list_descdialog?.adapter = adapter
        dialog.customView?.list_descdialog?.setOnItemClickListener { parent, view, position, id ->
            dialog.customView?.edit_descdialog_description?.setText(descriptionList[position])
        }
        dialog.show()
    }

}