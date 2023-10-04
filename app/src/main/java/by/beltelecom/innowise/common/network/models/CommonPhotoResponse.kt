package by.beltelecom.innowise.common.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonPhotoResponse(
    @SerialName("total_results")
    val total: Int? = null,
    val page: Int,
    @SerialName("per_page")
    val perPage: Int,
    val photos: List<PhotoModel>
)
