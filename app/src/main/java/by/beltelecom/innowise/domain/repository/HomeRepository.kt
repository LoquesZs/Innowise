package by.beltelecom.innowise.domain.repository

import by.beltelecom.innowise.domain.entities.CollectionDomain
import by.beltelecom.innowise.domain.entities.Photos
import io.reactivex.Single

interface HomeRepository {

    fun getCurated(page: Int = 1): Single<Photos>

    fun getFeaturedCollections(page: Int = 1): Single<List<CollectionDomain>>

    fun search(query: String, page: Int): Single<Photos>
}