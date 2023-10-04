package by.beltelecom.innowise.presentation.home

import by.beltelecom.innowise.domain.entities.CollectionDomain
import by.beltelecom.innowise.domain.entities.Photo

sealed class Source {
    data object Collections : Source()
    data class Search(val query: String) : Source()
    data object Featured : Source()

    override fun toString(): String {
        return when(this) {
            is Collections -> "Collections"
            is Featured -> "Featured"
            is Search -> "Search[query: $query]"
        }
    }
}

sealed class HomeUIState(
    val source: Source,
    val collections: List<CollectionDomain>?
) {

    class Loading(
        source: Source,
        collections: List<CollectionDomain>? = null
    ) : HomeUIState(source, collections)

    class Error(
        source: Source,
        collections: List<CollectionDomain>? = null
    ) : HomeUIState(source, collections)

    class Success(
        source: Source,
        collections: List<CollectionDomain>? = null,
        val photos: List<Photo>?,
        val page: Int,
        val total: Int? = null
    ) : HomeUIState(source, collections)

    override fun toString(): String {
        return when (this) {
            is Error -> "Error[source = $source, collections = $collections]"
            is Loading -> "Loading[source = $source, collections = $collections]"
            is Success -> "Success[source = $source, collections = $collections, photos: $photos, page: $page, total: $total]"
        }
    }
}
