package id.eve.jetpackcompose.domain.model

data class Note(
    val id: String = "",
    var title: String = "",
    var description: String = "",
    var timestamp: Long = 0
)