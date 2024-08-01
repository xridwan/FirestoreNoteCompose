package id.eve.jetpackcompose.data

import com.google.firebase.firestore.CollectionReference
import id.eve.jetpackcompose.data.response.NoteDto
import id.eve.jetpackcompose.domain.model.Note
import id.eve.jetpackcompose.domain.repository.NoteRepository

class NoteRepositoryImpl(
    private val db: CollectionReference
) : NoteRepository {

    override fun createNote(note: Note, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val newNote = note.copy(timestamp = System.currentTimeMillis())
        db.add(newNote).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { e ->
            onFailure(e)
        }
    }

    override fun readNotes(onSuccess: (List<Note>) -> Unit, onFailure: (Exception) -> Unit) {
        db.get().addOnSuccessListener { result ->
            val cours = mutableListOf<Note>()
            for (document in result) {
                val response = document.toObject(NoteDto::class.java).apply {
                    id = document.id
                }
                val course = response.toEntity()
                cours.add(course)
            }
            onSuccess(cours)
        }.addOnFailureListener { e ->
            onFailure(e)
        }
    }

    override fun updateNote(
        note: Note,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.document(note.id).set(note).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { e ->
            onFailure(e)
        }
    }

    override fun deleteNote(noteId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.document(noteId).delete().addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { e ->
            onFailure(e)
        }
    }
}