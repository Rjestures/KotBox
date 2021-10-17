package com.rjesture.kotbox.room.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.rjesture.kotbox.R
import com.rjesture.kotbox.room.database.entity.Note

class NoteAdapter(private val onItemClickListener: (Note) -> Unit) :
    ListAdapter<Note, NoteAdapter.NoteHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.roomlist, parent, false)
        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        with(getItem(position)) {
            holder.tv_title.text = title
            holder.tv_description.text = description
            holder.tv_priorityId.text = priority.toString()

        }
    }

    fun getNotesAt(position: Int) = getItem(position)
    inner class NoteHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_title: TextView = itemView.findViewById(R.id.tv_title)
        val tv_description: TextView = itemView.findViewById(R.id.tv_description)
        val tv_priorityId: TextView = itemView.findViewById(R.id.tv_priorityId)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != NO_POSITION)
                    onItemClickListener(getItem(adapterPosition))
            }
        }
    }


}

private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Note, newItem: Note) =
        oldItem.title == newItem.title && oldItem.description == newItem.description
                && oldItem.priority == newItem.priority

}






