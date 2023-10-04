package by.beltelecom.innowise.common.network.models

import kotlinx.serialization.Serializable

@Serializable
data class FeaturedCollections(
    val collections: List<CollectionModel>
)
