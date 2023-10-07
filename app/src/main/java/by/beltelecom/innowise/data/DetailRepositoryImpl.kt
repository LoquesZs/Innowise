package by.beltelecom.innowise.data

import by.beltelecom.innowise.common.database.dao.BookmarksDao
import by.beltelecom.innowise.common.database.dao.DetailDao
import by.beltelecom.innowise.common.network.PexelsApiService
import by.beltelecom.innowise.domain.entities.Photo
import by.beltelecom.innowise.domain.repository.DetailRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val pexelsApiService: PexelsApiService,
    private val detailDao: DetailDao,
    private val bookmarksDao: BookmarksDao
) : DetailRepository {

    override fun getPhoto(id: Long): Single<Photo> {
        return detailDao.getPhoto(id)
            .map { entity -> entity.toDomain() }
            .onErrorReturn { throwable ->
                pexelsApiService.getPhotoByID(id)
                    .map { model -> model.toDomain() }
                    .blockingGet()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun addBookmark(photo: Photo): Completable {
        return bookmarksDao.addBookmark(photo.toEntity())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun removeBookmark(photo: Photo): Completable {
        return bookmarksDao.removeBookmark(photo.toEntity())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getBookmarkState(id: Long): Flowable<Boolean> {
        return detailDao
            .getBookmarkState(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}