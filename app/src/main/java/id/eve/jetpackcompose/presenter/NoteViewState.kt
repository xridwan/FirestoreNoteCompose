package id.eve.jetpackcompose.presenter

import id.eve.jetpackcompose.domain.model.Note

data class NoteViewState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

fun NoteViewState.updateNotes(newNotes: List<Note>): NoteViewState {
    return NoteViewState(
        notes = newNotes,
        isLoading = this.isLoading,
        errorMessage = this.errorMessage
    )
}