package com.mcustodio.tasklog.view.folders

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.mcustodio.tasklog.R
import com.mcustodio.tasklog.model.folder.Folder
import com.mcustodio.tasklog.utils.Preferences
import com.mcustodio.tasklog.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_folders.*
import android.widget.Toast
import java.lang.Exception
import android.os.Build
import com.mcustodio.tasklog.model.AppDatabase
import com.opencsv.CSVWriter
import java.io.*


class FoldersActivity : AppCompatActivity() {


    private val viewModel by lazy { ViewModelProviders.of(this).get(FoldersViewModel::class.java) }
    private val adapter = FolderAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folders)
        setView()
        observeFolders()
        requestWritePermission()
    }


    override fun onResume() {
        super.onResume()
        launchLastFolderIfPossible()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_folder, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuitem_folder_export -> export()
        }
        return true
    }


    private fun setView() {
        recycler_folders.layoutManager = LinearLayoutManager(this)
        recycler_folders.adapter = adapter
        setAdapterClickListeners()
        fab_folders_add.setOnClickListener {
            FolderNameDialog(this, onNameSet).show()
        }
    }


    private fun requestWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permissions, 0)
        }
    }


    private fun setAdapterClickListeners() {
        adapter.onItemClick = { folder ->
            folder.id?.let { MainActivity.launch(this, it) }
        }

        adapter.onItemLongClick = { folder -> askToDelete(folder) }
    }


    private fun observeFolders() {
        viewModel.folders.observe(this, Observer { folders ->
            folders?.let { adapter.data = it }
        })
    }


    private fun launchLastFolderIfPossible() {
        val lastFolderId = Preferences(this).lastSelectedFolder
        if (lastFolderId >= 0) {
            MainActivity.launch(this, lastFolderId)
        }
    }


    private fun askToDelete(folder: Folder) {
        MaterialDialog.Builder(this)
                .title("Excluir?")
                .positiveText("Sim")
                .negativeText("Não")
                .onPositive { _, _ ->
                    viewModel.delete(folder)
                }
                .show()
    }


    private val onNameSet : (String) -> Unit = { name ->
        viewModel.createFolder(name)
    }


    // todo - Refatorar código para Repository
    private fun export() {
        val exportDir = File(Environment.getExternalStorageDirectory(), "")
        if (!exportDir.exists()) exportDir.mkdirs()

        val file = File(exportDir, "TaskLog - Report.csv")
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            val db = AppDatabase.getFrom(this).openHelper.readableDatabase
            val cursor = db.query("SELECT * FROM Task",null)
            csvWrite.writeNext(cursor.columnNames)
            while(cursor.moveToNext()) {
                //Which column you want to exprort
                val list = arrayListOf<String>()
                for (i in 0..(cursor.columnCount - 1)) {
                    list.add(cursor.getString(i))
                }
                csvWrite.writeNext(list.toTypedArray())
            }
            csvWrite.flush()
            csvWrite.close()
            cursor.close()

            val path = Uri.fromFile(file)
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "vnd.android.cursor.dir/email"
                putExtra(Intent.EXTRA_STREAM, path)
                putExtra(Intent.EXTRA_SUBJECT, "TaskLog - Report")
            }
            startActivity(Intent.createChooser(emailIntent , "Exportar para..."))

//            Toast.makeText(this, "SUCESSO", Toast.LENGTH_LONG).show()
        } catch(sqlEx: Exception) {
            requestWritePermission()
            Toast.makeText(this, sqlEx.message, Toast.LENGTH_LONG).show()
        }
    }
}
