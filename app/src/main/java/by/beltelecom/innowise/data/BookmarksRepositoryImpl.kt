package by.beltelecom.innowise.data

import by.beltelecom.innowise.common.database.dao.BookmarksDao
import by.beltelecom.innowise.domain.entities.Photo
import by.beltelecom.innowise.domain.repository.BookmarksRepository
import io.reactivex.Flowable
import javax.inject.Inject

class BookmarksRepositoryImpl @Inject constructor(
    private val dao: BookmarksDao
) : BookmarksRepository {

    override fun getBookmarks(): Flowable<List<Photo>> {
        return dao.getBookmarks().map { photos ->
            photos.map { entity ->
                entity.toDomain()
            }
        }
    }
}