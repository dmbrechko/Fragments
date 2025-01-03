package com.example.fragments.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragments.room.Note
import com.example.fragments.room.NoteToInsert
import com.example.fragments.room.NotesDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {
    private val notesDao = NotesDatabase.getDB(getApplication<Application>().applicationContext).notesDao()
    private val _notesFlow = notesDao.getAllNotesFlow().stateIn(viewModelScope, SharingStarted.Eagerly, listOf())
    val notesFlow: Flow<List<Note>> = _notesFlow

    fun addNote(note: NoteToInsert) {
        viewModelScope.launch {
            notesDao.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            notesDao.updateNote(note)
        }
    }
}