package com.example.fragments.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fragments.room.Note
import com.example.fragments.room.NotesDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class DetailsViewModel(application: Application, noteId: Long): AndroidViewModel(application) {
    private val notesDao = NotesDatabase.getDB(getApplication<Application>().applicationContext).notesDao()
    private val _noteFlow = notesDao.getNote(noteId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val noteFlow: Flow<Note> = _noteFlow.filterNotNull()
    val note get() = _noteFlow.value

    fun updateNote(note: Note) {
        viewModelScope.launch {
            notesDao.updateNote(note)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class DetailsModelFactory(private val application: Application, private val noteId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(application, noteId) as T
    }
}