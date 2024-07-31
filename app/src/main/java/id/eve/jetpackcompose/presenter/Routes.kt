package id.eve.jetpackcompose.presenter

sealed class Routes(
    val route: String
) {
    data object NoteListScreen : Routes("CourseList")
    data object NoteScreen : Routes("Course")
}