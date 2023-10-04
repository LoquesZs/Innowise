package by.beltelecom.innowise.domain.usecases.home

import by.beltelecom.innowise.domain.entities.CollectionDomain
import by.beltelecom.innowise.domain.repository.HomeRepository
import io.reactivex.Single
import javax.inject.Inject

class GetCollectionsUseCase @Inject constructor(
    private val repository: HomeRepository
) {

    operator fun invoke(): Single<List<CollectionDomain>> {
        return repository.getFeaturedCollections()
    }
}