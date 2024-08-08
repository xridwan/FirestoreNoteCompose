package id.eve.jetpackcompose.domain.usecase

import id.eve.jetpackcompose.data.FirebaseResponse
import id.eve.jetpackcompose.domain.model.Note
import id.eve.jetpackcompose.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class AddNote(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(note: Note): Flow<FirebaseResponse<Boolean>> {
        return noteRepository.createNote(note)
    }
}

class GetNotes(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(): Flow<FirebaseResponse<List<Note>>> {
        return noteRepository.getNotes()
    }
}

class UpdateNote(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(note: Note): Flow<FirebaseResponse<Boolean>> {
        return noteRepository.updateNote(note)
    }
}

class DeleteNote(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(noteId: String): Flow<FirebaseResponse<Boolean>> {
        return noteRepository.deleteNote(noteId)
    }
}

data class NoteUseCase(
    val addNote: AddNote,
    val getNotes: GetNotes,
    val updateNote: UpdateNote,
    val deleteNote: DeleteNote
)