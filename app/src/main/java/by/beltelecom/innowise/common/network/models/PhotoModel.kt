package by.beltelecom.innowise.common.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoModel(
    val id: Long,
    val width: Int,
    val height: Int,
    val photographer: String,
    @SerialName("src")
    val sources: SourceModel
)

@Serializable
data class SourceModel(
    val original: String,
    val portrait: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String
)
