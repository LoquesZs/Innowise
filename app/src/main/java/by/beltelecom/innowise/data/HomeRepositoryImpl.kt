package by.beltelecom.innowise.data

import by.beltelecom.innowise.common.network.PexelsApiService
import by.beltelecom.innowise.domain.entities.CollectionDomain
import by.beltelecom.innowise.domain.entities.Photos
import by.beltelecom.innowise.domain.repository.HomeRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val pexelsApiService: PexelsApiService
) : HomeRepository {
    override fun getCurated(page: Int): Single<Photos> {
        return pexelsApiService.popular(page)
            .map { it.toDomain() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFeaturedCollections(page: Int): Single<List<CollectionDomain>> {
        return pexelsApiService.featuredCollections(page = page)
            .map { featuredCollections -> featuredCollections.collections.map { collection -> collection.toDomain() } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun search(query: String, page: Int): Single<Photos> {
        return pexelsApiService.search(query, page)
            .map { it -> it.toDomain() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}