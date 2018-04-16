package com.mcustodio.tasklog.view.folders

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.mcustodio.tasklog.R
import com.mcustodio.tasklog.model.folder.Folder
import kotlinx.android.synthetic.main.dialog_folders.view.*

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
        dialog.show()
    }
}