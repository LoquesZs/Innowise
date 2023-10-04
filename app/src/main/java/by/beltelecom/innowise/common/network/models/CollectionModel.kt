package by.beltelecom.innowise.common.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionModel(
    val id: String,
    val title: String,
    val description: String,
    @SerialName("media_count")
    val mediaCount: Int,
    @SerialName("photos_count")
    val photosCount: Int,
    @SerialName("videos_count")
    val videosCount: Int
)
