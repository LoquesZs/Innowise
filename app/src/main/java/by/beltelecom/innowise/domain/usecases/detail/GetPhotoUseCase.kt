package by.beltelecom.innowise.domain.usecases.detail

import by.beltelecom.innowise.domain.entities.Photo
import by.beltelecom.innowise.domain.repository.DetailRepository
import io.reactivex.Single
import javax.inject.Inject

class GetPhotoUseCase @Inject constructor(
    private val repository: DetailRepository
) {

    operator fun invoke(id: Long): Single<Photo> {
        return repository.getPhoto(id)
    }
}