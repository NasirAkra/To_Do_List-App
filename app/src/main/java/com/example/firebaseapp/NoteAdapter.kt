package com.example.firebaseapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.databinding.NotesItemsBinding

class NoteAdapter(private val notes: List<NoteItem>,private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    interface OnItemClickListener{
        fun onDeleteClick(noteId: String?)
        fun onUpdateClick(noteId: String?,title: String,description:String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        val binding = NotesItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
       holder.binding.update.setOnClickListener {
           itemClickListener.onUpdateClick(note.noteId, note.title.toString(), note.content.toString())
       }
        holder.binding.Delete.setOnClickListener {
            itemClickListener.onDeleteClick(note.noteId)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(val binding: NotesItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteItem) {
            binding.apply {
                titleText.text = note.title
                descriptionText.text = note.content
            }
        }
    }
}