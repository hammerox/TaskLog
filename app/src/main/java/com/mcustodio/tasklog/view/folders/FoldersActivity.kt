package com.mcustodio.tasklog.view.folders

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mcustodio.tasklog.R
import com.mcustodio.tasklog.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_folders.*

class FoldersActivity : AppCompatActivity() {


    private val viewModel by lazy { ViewModelProviders.of(this).get(FoldersViewModel::class.java) }
    private val adapter = FolderAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folders)
        setView()
        observeFolders()
    }


    private fun setView() {
        recycler_folders.layoutManager = LinearLayoutManager(this)
        recycler_folders.adapter = adapter
        setAdapterClickListeners()
        fab_folders_add.setOnClickListener {
            FolderNameDialog(this, onNameSet).show()
        }
    }


    private fun setAdapterClickListeners() {
        adapter.onItemClick = { folder ->
            folder.id?.let { MainActivity.launch(this, it) }
        }

        adapter.onItemLongClick = { folder ->
            viewModel.delete(folder)
        }
    }


    private fun observeFolders() {
        viewModel.folders.observe(this, Observer { folders ->
            folders?.let { adapter.data = it }
        })
    }


    private val onNameSet : (String) -> Unit = { name ->
        viewModel.createFolder(name)
    }
}
