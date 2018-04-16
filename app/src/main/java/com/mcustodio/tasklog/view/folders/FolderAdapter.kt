package com.mcustodio.tasklog.view.folders

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mcustodio.tasklog.R
import com.mcustodio.tasklog.model.folder.Folder
import kotlinx.android.synthetic.main.item_folder.view.*

class FolderAdapter : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    var onItemClick: ((Folder) -> Unit)? = null
    var onItemLongClick: ((Folder) -> Unit)? = null

    var data : List<Folder> = listOf()
        set(value) {
            field = value.sortedBy { it.createdDate }
            notifyDataSetChanged()
        }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_folder, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = data[position]
        holder.setView(folder)
        holder.setClickListener(onItemClick, folder)
        holder.setLongClickListener(onItemLongClick, folder)
    }


    inner class FolderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val card = view.card_folderitem
        private val name = view.text_folderitem_name


        fun setView(folder: Folder) {
            name.text = folder.name
        }


        fun setClickListener(onItemClick: ((Folder) -> Unit)?, folder: Folder) {
            card.setOnClickListener { onItemClick?.invoke(folder) }
        }


        fun setLongClickListener(onItemLongClick: ((Folder) -> Unit)?, folder: Folder) {
            card.setOnLongClickListener {
                onItemLongClick?.invoke(folder)
                true
            }
        }
    }
}