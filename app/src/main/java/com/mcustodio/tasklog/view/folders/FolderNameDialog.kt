package com.mcustodio.tasklog.view.folders

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.mcustodio.tasklog.R
import com.mcustodio.tasklog.utils.toString
import kotlinx.android.synthetic.main.dialog_folders.view.*
import java.util.*

class FolderNameDialog(activity: Activity, onSelect: ((String) -> Unit)) {

    private val dialog = MaterialDialog.Builder(activity)
            .title("Defina o nome")
            .customView(R.layout.dialog_folders, false)
            .positiveText("OK")
            .onPositive { dialog, _ ->
                val description = dialog.view.edit_folderdesc_name.text.toString()
                onSelect(description)
            }
            .build()



    fun show() {
        val now = Calendar.getInstance().time
        dialog.customView?.edit_folderdesc_name?.setText(now.toString("dd/MM - '$weekName'"))
        dialog.show()
    }


    private val weekName = when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
        0 -> "Sab"
        1 -> "Dom"
        2 -> "Seg"
        3 -> "Ter"
        4 -> "Qua"
        5 -> "Qui"
        6 -> "Sex"
        else -> "???"
    }
}