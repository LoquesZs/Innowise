package by.beltelecom.innowise.domain.entities

data class Photo(
    val id: Long,
    val width: Int,
    val height: Int,
    val photographer: String,
    val sources: Source
)

data class Source(
    val original: String,
    val portrait: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String
)