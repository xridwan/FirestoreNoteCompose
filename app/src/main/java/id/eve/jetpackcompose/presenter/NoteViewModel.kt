package id.eve.jetpackcompose.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.eve.jetpackcompose.domain.model.Note
import id.eve.jetpackcompose.domain.usecase.NoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCase: NoteUseCase
) : ViewModel() {

    var noteTitle by mutableStateOf("")
    var noteDescription by mutableStateOf("")
    private var updateNote: Note? by mutableStateOf(null)


    fun onCourseNameChange(newName: String) {
        noteTitle = newName
    }

    fun onCourseDescriptionChange(newDescription: String) {
        noteDescription = newDescription
    }

    fun setEditingCourse(note: Note?) {
        updateNote = note
        noteTitle = note?.title ?: ""
        noteDescription = note?.description ?: ""
    }

    fun isFormFilled(): Boolean {
        return noteTitle.isNotBlank() && noteDescription.isNotBlank()
    }

    private fun clearForm() {
        noteTitle = ""
        noteDescription = ""
    }

    fun saveNote(showToast: (String) -> Unit) {
        val note = Note(
            id = updateNote?.id ?: UUID.randomUUID().toString(),
            title = noteTitle,
            description = noteDescription,
            timestamp = System.currentTimeMillis()
        )

        if (updateNote == null) {
            noteUseCase.addNote(note, {
                showToast("Your Note has been added to Firebase Firestore")
                clearForm()
            }, { e ->
                showToast("Fail to add note \n$e")
            })
        } else {
            noteUseCase.updateNote(note, {
                showToast("Your Course has been updated")
                clearForm()
            }, { e ->
                showToast("Fail to update course \n$e")
            })
        }
    }

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> get() = _notes

    fun fetchNotes(showToast: (String) -> Unit) {
        noteUseCase.getNotes({ courseList ->
            _notes.value = courseList
        }, { e ->
            showToast("Fail to get note \n$e")
        })
    }

    fun deleteNote(courseId: String, showToast: (String) -> Unit) {
        noteUseCase.deleteNote(courseId, {
            showToast("Note has been deleted")
            fetchNotes {
                showToast(it)
            }
        }, { e ->
            showToast("Fail to delete note \n$e")
        })
    }
}