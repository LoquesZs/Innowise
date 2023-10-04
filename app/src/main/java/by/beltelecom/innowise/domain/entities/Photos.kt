package by.beltelecom.innowise.domain.entities


data class Photos(
    val page: Int,
    val photos: List<Photo>,
    val total: Int? = null
)
