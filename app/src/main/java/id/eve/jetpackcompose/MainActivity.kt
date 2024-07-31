package id.eve.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import id.eve.jetpackcompose.presenter.notelist.CourseListScreen
import id.eve.jetpackcompose.presenter.note.NoteScreen
import id.eve.jetpackcompose.presenter.NoteViewModel
import id.eve.jetpackcompose.presenter.Routes
import id.eve.jetpackcompose.ui.theme.JetpackComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackComposeTheme {
                Surface {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                        noteViewModel = noteViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    noteViewModel: NoteViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.NoteListScreen.route
    ) {
        composable(Routes.NoteScreen.route) {
            NoteScreen(
                noteViewModel = noteViewModel,
                context = LocalContext.current
            ){
                navController.navigateUp()
            }
        }
        composable(Routes.NoteListScreen.route) {
            CourseListScreen(
                noteViewModel = noteViewModel,
                context = LocalContext.current
            ) {
                navController.navigate(Routes.NoteScreen.route)
            }
        }
    }
}

