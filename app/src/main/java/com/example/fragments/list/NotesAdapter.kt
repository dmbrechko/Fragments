package com.example.fragments.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fragments.R
import com.example.fragments.databinding.ListItemBinding
import com.example.fragments.room.Note
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class NotesAdapter(private val actions: NoteItemActionable): ListAdapter<Note, NotesAdapter.NoteViewHolder>(DiffUtilNoteCallback()) {
    private val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(parent.context, binding, actions, formatter)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface NoteItemActionable {
        fun onCheckedChanged(isChecked: Boolean, note: Note)
        fun onEdit(noteId: Long)
        fun onDelete(note: Note)
    }

    class NoteViewHolder(val context: Context,
                         val binding: ListItemBinding,
                         val actions: NoteItemActionable,
                         val formatter: DateTimeFormatter): RecyclerView.ViewHolder(binding.root) {
        private lateinit var note: Note

        init {
            binding.apply {
                noteCB.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked != note.isDone) {
                        actions.onCheckedChanged(isChecked, note)
                    }
                }
                editIV.setOnClickListener {
                    actions.onEdit(note.id)
                }
                deleteIV.setOnClickListener {
                    actions.onDelete(note)
                }
            }
        }

        fun bind(newNote: Note) {
            note = newNote
            binding.apply {
                noteCB.text = String.format(context.getString(R.string.note), note.id, note.text)
                noteCB.isChecked = note.isDone
                dateTimeTV.text = formatter.format(note.whenCreated)
            }
        }
    }

    class DiffUtilNoteCallback: DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}



