package id.eve.jetpackcompose.domain.usecase

import id.eve.jetpackcompose.domain.model.Note
import id.eve.jetpackcompose.domain.repository.NoteRepository

class NoteInteractor(
    private val noteRepository: NoteRepository
) : NoteUseCase {

    override fun addNote(note: Note, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        noteRepository.createNote(note, onSuccess, onFailure)
    }

    override fun getNotes(onSuccess: (List<Note>) -> Unit, onFailure: (Exception) -> Unit) {
        noteRepository.readNotes(onSuccess, onFailure)
    }

    override fun deleteNote(noteId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        noteRepository.deleteNote(noteId, onSuccess, onFailure)
    }

    override fun updateNote(
        note: Note,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        noteRepository.updateNote(note, onSuccess, onFailure)
    }
}