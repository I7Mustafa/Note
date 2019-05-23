package com.i7mustafa.notes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase :RoomDatabase(){

    abstract fun getNoteDao() : NoteDao

    companion object {

        @Volatile private var instances: NoteDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instances?: synchronized(LOCK){
            instances?: buldDatabase(context).also { instances = it }
        }

        private fun buldDatabase (context: Context) = Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            "noteDataBase"
        ).build()
    }

}