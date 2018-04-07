package com.mcustodio.tasklog.view.main

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.mcustodio.tasklog.R
import com.mcustodio.tasklog.model.task.Task
import kotlinx.android.synthetic.main.dialog_counter.view.*

/**
 * Created by logonrm on 07/04/2018.
 */
class DescriptionDialog(activity: Activity, onSelect: ((String) -> Unit)) {


    val dialog = MaterialDialog.Builder(activity)
            .title("Descreva")
            .customView(R.layout.dialog_counter, false)
            .positiveText("OK")
            .onPositive { dialog, _ ->
                val description = dialog.view.edit_counterdialog_description.text.toString()
                onSelect(description)
            }
            .build()


    fun show(task: Task?) {
        dialog.customView?.edit_counterdialog_description?.setText(task?.description)
        dialog.show()
    }

}