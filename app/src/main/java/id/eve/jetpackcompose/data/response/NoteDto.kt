package id.eve.jetpackcompose.data.response

import id.eve.jetpackcompose.domain.model.Note

data class NoteDto(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: Long = 0
) {
    fun toEntity(): Note {
        return Note(
            id = id,
            title = title,
            description = description,
            timestamp = timestamp
        )
    }
}
