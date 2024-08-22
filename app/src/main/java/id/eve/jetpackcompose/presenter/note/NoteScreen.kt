package id.eve.jetpackcompose.presenter.note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.eve.jetpackcompose.presenter.NoteInputField
import id.eve.jetpackcompose.presenter.NoteViewModel

@Composable
fun NoteScreen(
    noteViewModel: NoteViewModel,
    navigateToBack: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    BackHandler {
        if (noteViewModel.isFormFilled()) {
            showDialog = true
        } else {
            navigateToBack()
        }
    }

    if (showDialog) {
        SaveNoteDialog(
            onConfirm = {
                if (noteViewModel.saveNote()) {
                    showDialog = false
                    navigateToBack()
                }
            },
            onDismiss = { showDialog = false }
        )
    }

    NoteContent(
        noteViewModel = noteViewModel,
        onBackClick = {
            if (noteViewModel.isFormFilled()) {
                showDialog = true
            } else {
                navigateToBack()
            }
        }
    )
}

@Composable
fun SaveNoteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Save Note",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                "Do you want to save the note before exiting?",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    "OK",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(
                    "Cancel",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteContent(
    noteViewModel: NoteViewModel,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier.clickable(onClick = onBackClick)
                    )
                },
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NoteInputField(
                value = noteViewModel.noteTitle,
                onValueChange = noteViewModel::onNoteTitleChange,
                placeholder = "Title",
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp,
                )
            )
            NoteInputField(
                value = noteViewModel.noteDescription,
                onValueChange = noteViewModel::onNoteDescriptionChange,
                placeholder = "Start typing...",
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    letterSpacing = 0.5.sp,
                ),
                singleLine = false,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}





