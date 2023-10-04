package by.beltelecom.innowise.domain.usecases.bookmarks

import by.beltelecom.innowise.domain.entities.Photo
import by.beltelecom.innowise.domain.repository.BookmarksRepository
import io.reactivex.Flowable
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(
    private val repository: BookmarksRepository
) {

    operator fun invoke(): Flowable<List<Photo>> {
        return repository.getBookmarks()
    }
}