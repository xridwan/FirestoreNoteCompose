package id.eve.jetpackcompose.domain.repository

import id.eve.jetpackcompose.domain.model.Note

interface NoteRepository {
    fun createNote(note: Note, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
    fun readNotes(onSuccess: (List<Note>) -> Unit, onFailure: (Exception) -> Unit)
    fun updateNote(note: Note, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
    fun deleteNote(noteId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
}