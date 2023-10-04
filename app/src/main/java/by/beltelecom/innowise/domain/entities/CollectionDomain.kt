package by.beltelecom.innowise.domain.entities

data class CollectionDomain(
    val id: String,
    val title: String,
    val description: String,
    val mediaCount: Int,
    val photosCount: Int,
    val videosCount: Int
)