package id.eve.jetpackcompose.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.eve.jetpackcompose.data.FirebaseResponse
import id.eve.jetpackcompose.domain.model.Note
import id.eve.jetpackcompose.domain.usecase.NoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCase: NoteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NoteViewState())
    val state: StateFlow<NoteViewState> = _state

    var noteTitle by mutableStateOf("")
    var noteDescription by mutableStateOf("")

    private var updateNote: Note? by mutableStateOf(null)

    init {
        getNotes()
    }

    fun onNoteTitleChange(newTitle: String) {
        noteTitle = newTitle
    }

    fun onNoteDescriptionChange(newDescription: String) {
        noteDescription = newDescription
    }

    fun setEditingNote(note: Note?) {
        updateNote = note
        noteTitle = note?.title ?: ""
        noteDescription = note?.description ?: ""
    }

    fun isFormFilled(): Boolean {
        return noteTitle.isNotBlank() && noteDescription.isNotBlank()
    }

    private fun clearForm() {
        updateNote = null
        noteTitle = ""
        noteDescription = ""
    }

    fun saveNote(): Boolean {
        val note = Note(
            id = updateNote?.id ?: UUID.randomUUID().toString(),
            title = noteTitle,
            description = noteDescription,
            timestamp = System.currentTimeMillis()
        )

        return if (updateNote == null) createNote(note)
        else updateNote(note)
    }

    private fun updateState(
        isLoading: Boolean = false,
        notes: List<Note>? = null,
        errorMessage: String? = null
    ) {
        _state.value = _state.value.copy(
            isLoading = isLoading,
            notes = notes ?: _state.value.notes,
            errorMessage = errorMessage
        )
    }

    fun <T> handleResponse(response: FirebaseResponse<T>, onSuccess: (T) -> Unit) {
        when (response) {
            is FirebaseResponse.Loading -> updateState(isLoading = true)
            is FirebaseResponse.Success -> {
                updateState(isLoading = false)
                onSuccess(response.data)
            }

            is FirebaseResponse.Error -> updateState(
                isLoading = false,
                errorMessage = response.errorMessage
            )
        }
    }

    private fun createNote(note: Note): Boolean {
        viewModelScope.launch {
            noteUseCase.addNote(note).collect { value ->
                handleResponse(value) {
                    if (it) {
                        clearForm()
                    }
                }
            }
        }
        return true
    }

    private fun getNotes() {
        viewModelScope.launch {
            noteUseCase.getNotes().collect { value ->
                handleResponse(value) { notes ->
                    updateState(notes = notes)
                }
            }
        }
    }

    private fun updateNote(note: Note): Boolean {
        viewModelScope.launch {
            noteUseCase.updateNote(note).collect { value ->
                handleResponse(value) {
                    if (it) {
                        clearForm()
                    }
                }
            }
        }
        return true
    }

    fun deleteNote(noteId: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            noteUseCase.deleteNote(noteId).collect { value ->
                handleResponse(value) {
                    if (it) onSuccess("Berhasil menghapus catatan")
                }
            }
        }
    }
}
