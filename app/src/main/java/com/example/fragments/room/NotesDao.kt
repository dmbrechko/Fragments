package com.example.fragments.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NotesDao {
    @Insert(entity = Note::class)
    abstract suspend fun insertNote(note: NoteToInsert)

    @Update
    abstract suspend fun updateNote(note: Note)

    @Delete
    abstract suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes ORDER BY when_created")
    abstract fun getAllNotesFlow(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    abstract fun getNote(noteId: Long): Flow<Note>
}