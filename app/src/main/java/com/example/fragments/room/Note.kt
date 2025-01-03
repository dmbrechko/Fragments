package com.example.fragments.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "notes")
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val text: String,
    @ColumnInfo(name = "is_done", defaultValue = "0")
    val isDone: Boolean = false,
    @ColumnInfo(name = "when_created")
    val whenCreated: LocalDateTime = LocalDateTime.now()
)

data class NoteToInsert(
    val text: String,
    @ColumnInfo(name = "when_created")
    val whenCreated: LocalDateTime = LocalDateTime.now()
)