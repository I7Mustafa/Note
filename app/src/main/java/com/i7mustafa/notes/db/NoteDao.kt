package com.i7mustafa.notes.db

import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    suspend fun addNote(note: Note)

    @Query ("SELECT * FROM Note ORDER BY ID DESC")
    suspend fun getAllNote() : List<Note>

    @Insert
    suspend fun addMultipleNote(vararg note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}