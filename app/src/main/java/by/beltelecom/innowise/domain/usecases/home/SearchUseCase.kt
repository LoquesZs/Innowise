package by.beltelecom.innowise.domain.usecases.home

import by.beltelecom.innowise.domain.entities.Photos
import by.beltelecom.innowise.domain.repository.HomeRepository
import io.reactivex.Single
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: HomeRepository
) {

    operator fun invoke(query: String, page: Int): Single<Photos> {
        return repository.search(query, page)
    }
}