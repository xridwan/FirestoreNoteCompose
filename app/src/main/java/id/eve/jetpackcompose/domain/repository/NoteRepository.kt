package id.eve.jetpackcompose.domain.repository

import id.eve.jetpackcompose.data.FirebaseResponse
import id.eve.jetpackcompose.domain.model.Note
import kotlinx.coroutines.flow.Flow

//interface NoteRepository {
//    fun createNote(note: Note, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
//    fun readNotes(onSuccess: (List<Note>) -> Unit, onFailure: (Exception) -> Unit)
//    fun updateNote(note: Note, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
//    fun deleteNote(noteId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
//}

interface NoteRepository {
    fun createNote(note: Note): Flow<FirebaseResponse<Boolean>>
    fun getNotes(): Flow<FirebaseResponse<List<Note>>>
    fun updateNote(note: Note): Flow<FirebaseResponse<Boolean>>
    fun deleteNote(noteId: String): Flow<FirebaseResponse<Boolean>>
}