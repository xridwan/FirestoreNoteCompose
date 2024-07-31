package id.eve.jetpackcompose.domain.usecase

import id.eve.jetpackcompose.domain.model.Note

interface NoteUseCase {
    fun addNote(note: Note, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
    fun getNotes(onSuccess: (List<Note>) -> Unit, onFailure: (Exception) -> Unit)
    fun deleteNote(noteId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
    fun updateNote(note: Note, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
}