package com.example.fragments.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDateTime
import java.time.ZoneOffset

@Database(entities = [Note::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun notesDao(): NotesDao
    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null
        fun getDB(context: Context): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: run {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotesDatabase::class.java,
                        "notes_database"
                    ).build()

                    INSTANCE = instance
                    instance
                }
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun localDateTimeToLong(dt: LocalDateTime): Long {
        return dt.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun longToLocalDateTime(seconds: Long): LocalDateTime {
        return LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC)
    }
}