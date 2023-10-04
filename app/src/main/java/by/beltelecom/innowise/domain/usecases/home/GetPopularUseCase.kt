package by.beltelecom.innowise.domain.usecases.home

import by.beltelecom.innowise.domain.entities.Photos
import by.beltelecom.innowise.domain.repository.HomeRepository
import io.reactivex.Single
import javax.inject.Inject

class GetPopularUseCase @Inject constructor(
    private val repository: HomeRepository
) {

    operator fun invoke(page: Int): Single<Photos> {
        return repository.getCurated(page)
    }
}