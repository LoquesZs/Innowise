package by.beltelecom.innowise.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava2.flowable
import by.beltelecom.innowise.common.database.dao.BookmarksDao
import by.beltelecom.innowise.domain.entities.Photo
import by.beltelecom.innowise.domain.repository.BookmarksRepository
import io.reactivex.Flowable
import javax.inject.Inject

class BookmarksRepositoryImpl @Inject constructor(
    private val dao: BookmarksDao
) : BookmarksRepository {

    override fun getBookmarks(): Flowable<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = true,
                maxSize = 40,
                prefetchDistance = 5,
                initialLoadSize = 30
            ),
            pagingSourceFactory = {
                dao.getBookmarks()
            }
        ).flowable.map { pagingData ->
            pagingData.map { entity ->
                entity.toDomain()
            }
        }
    }
}