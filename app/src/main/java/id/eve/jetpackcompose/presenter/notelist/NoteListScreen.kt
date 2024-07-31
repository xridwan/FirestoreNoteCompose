package id.eve.jetpackcompose.presenter.notelist

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.eve.jetpackcompose.presenter.NoteViewModel
import id.eve.jetpackcompose.utils.Utils.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    context: Context,
    noteViewModel: NoteViewModel,
    navigateToAdd: () -> Unit
) {
    val notes by remember { noteViewModel.notes }.collectAsState()

    LaunchedEffect(Unit) {
        noteViewModel.fetchNotes {
            context.showToast(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 8.dp),
                title = { Text(text = "Firestore Note") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToAdd) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Note is empty")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = paddingValues,
                modifier = Modifier.padding(16.dp)
            ) {
                items(notes) { note ->
                    NoteListItem(
                        note = note,
                        deleteNote = { noteId ->
                            noteViewModel.deleteNote(noteId) {
                                context.showToast(it)
                            }
                        },
                        updateNote = {
                            noteViewModel.setEditingCourse(it)
                            navigateToAdd()
                        }
                    )
                }
            }
        }
    }
}

