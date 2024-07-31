package id.eve.jetpackcompose.data.response

import id.eve.jetpackcompose.domain.model.Note

data class NoteResponse(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: Long = 0
) {
    fun mappingToCourse(): Note {
        return Note(
            id = id,
            title = title,
            description = description,
            timestamp = timestamp
        )
    }
}
