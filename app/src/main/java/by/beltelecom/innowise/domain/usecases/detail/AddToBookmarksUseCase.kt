package by.beltelecom.innowise.domain.usecases.detail

import by.beltelecom.innowise.domain.entities.Photo
import by.beltelecom.innowise.domain.repository.DetailRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class AddToBookmarksUseCase @Inject constructor(
    private val repository: DetailRepository
) {

    operator fun invoke(photo: Photo): Completable {
        return repository.addBookmark(photo)
    }
}