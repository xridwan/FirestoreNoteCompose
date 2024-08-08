package id.eve.jetpackcompose.data

sealed class FirebaseResponse<out R> {
    data class Success<out T>(val data: T) : FirebaseResponse<T>()
    data class Error(val errorMessage: String) : FirebaseResponse<Nothing>()
    data object Loading : FirebaseResponse<Nothing>()
}