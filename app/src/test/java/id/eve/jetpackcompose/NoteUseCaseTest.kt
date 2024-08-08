package id.eve.jetpackcompose

import id.eve.jetpackcompose.data.FirebaseResponse
import id.eve.jetpackcompose.domain.model.Note
import id.eve.jetpackcompose.domain.repository.NoteRepository
import id.eve.jetpackcompose.domain.usecase.AddNote
import id.eve.jetpackcompose.domain.usecase.DeleteNote
import id.eve.jetpackcompose.domain.usecase.GetNotes
import id.eve.jetpackcompose.domain.usecase.UpdateNote
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NoteUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var addNote: AddNote
    private lateinit var getNotes: GetNotes
    private lateinit var updateNote: UpdateNote
    private lateinit var deleteNote: DeleteNote

    @Before
    fun setUp() {
        noteRepository = mockk()
        addNote = AddNote(noteRepository)
        getNotes = GetNotes(noteRepository)
        updateNote = UpdateNote(noteRepository)
        deleteNote = DeleteNote(noteRepository)
    }

    @Test
    fun `invoke should return success when note is added`() = runTest {
        val note = Note(
            id = "1",
            title = "Test Note",
            description = "Description",
            timestamp = 1234567890L
        )

        coEvery { noteRepository.createNote(note) } returns flowOf(FirebaseResponse.Success(true))

        val result = addNote.invoke(note)

        result.collect { response ->
            assertEquals(FirebaseResponse.Success(true), response)
        }

        coVerify { noteRepository.createNote(note) }
    }

    @Test
    fun `invoke should return success with list of notes`() = runTest {
        val notes = listOf(
            Note(
                id = "1",
                title = "Test Note 1",
                description = "Description 1",
                timestamp = 1234567890L
            ),
            Note(
                id = "2",
                title = "Test Note 2",
                description = "Description 2",
                timestamp = 1234567891L
            )
        )

        coEvery { noteRepository.getNotes() } returns flowOf(FirebaseResponse.Success(notes))

        val result = getNotes.invoke()

        result.collect { response ->
            assertEquals(FirebaseResponse.Success(notes), response)
        }

        coVerify { noteRepository.getNotes() }
    }

    @Test
    fun `invoke should return success when note is updated`() = runTest {
        val note = Note(
            id = "1",
            title = "Updated Note",
            description = "Updated Description",
            timestamp = 1234567890L
        )

        coEvery { noteRepository.updateNote(note) } returns flowOf(FirebaseResponse.Success(true))

        val result = updateNote.invoke(note)

        result.collect { response ->
            assertEquals(FirebaseResponse.Success(true), response)
        }

        coVerify { noteRepository.updateNote(note) }
    }

    @Test
    fun `invoke should return success when note is deleted`() = runTest {
        val noteId = "1"

        coEvery { noteRepository.deleteNote(noteId) } returns flowOf(FirebaseResponse.Success(true))

        val result = deleteNote.invoke(noteId)

        result.collect { response ->
            assertEquals(FirebaseResponse.Success(true), response)
        }

        coVerify { noteRepository.deleteNote(noteId) }
    }
}