package id.eve.jetpackcompose.data

import com.google.firebase.firestore.CollectionReference
import id.eve.jetpackcompose.data.response.NoteDto
import id.eve.jetpackcompose.domain.model.Note
import id.eve.jetpackcompose.domain.repository.NoteRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class NoteRepositoryImpl(
    private val db: CollectionReference
) : NoteRepository {

    override fun createNote(note: Note): Flow<FirebaseResponse<Boolean>> = flow {
        emit(FirebaseResponse.Loading)
        try {
            val newNote = note.copy(timestamp = System.currentTimeMillis())
            db.add(newNote).await()
            emit(FirebaseResponse.Success(true))
        } catch (e: Exception) {
            emit(FirebaseResponse.Error(e.message ?: "Unknown error"))
        }
    }

    override fun getNotes(): Flow<FirebaseResponse<List<Note>>> = flow {
        emit(FirebaseResponse.Loading)
        try {
            val notesFlow = getNotesFromSnapshot()
            notesFlow.collect { response ->
                emit(response)
            }
        } catch (e: Exception) {
            emit(FirebaseResponse.Error(e.message.toString()))
        }
    }

    override fun updateNote(note: Note): Flow<FirebaseResponse<Boolean>> = flow {
        emit(FirebaseResponse.Loading)
        try {
            db.document(note.id).set(note).await()
            emit(FirebaseResponse.Success(true))
        } catch (e: Exception) {
            emit(FirebaseResponse.Error(e.message ?: "Unknown error"))
        }
    }

    override fun deleteNote(noteId: String): Flow<FirebaseResponse<Boolean>> = flow {
        emit(FirebaseResponse.Loading)
        try {
            db.document(noteId).delete().await()
            emit(FirebaseResponse.Success(true))
        } catch (e: Exception) {
            emit(FirebaseResponse.Error(e.message ?: "Unknown error"))
        }
    }

    private fun getNotesFromSnapshot(): Flow<FirebaseResponse<List<Note>>> = callbackFlow {
        val snapshotListener = db.orderBy("timestamp").addSnapshotListener { snapshot, e ->
            val response = if (snapshot != null) {
                val notes = snapshot.documents.map { document ->
                    document.toObject(NoteDto::class.java)?.apply {
                        id = document.id
                    }?.toEntity() ?: Note()
                }
                FirebaseResponse.Success(notes)
            } else {
                FirebaseResponse.Error(e?.message ?: "Unknown error")
            }
            trySend(response).isSuccess
        }
        awaitClose { snapshotListener.remove() }
    }
}
