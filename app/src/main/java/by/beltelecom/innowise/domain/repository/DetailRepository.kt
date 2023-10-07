package by.beltelecom.innowise.domain.repository

import by.beltelecom.innowise.domain.entities.Photo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface DetailRepository {

    fun getPhoto(id: Long): Single<Photo>

    fun addBookmark(photo: Photo): Completable

    fun removeBookmark(photo: Photo): Completable

    fun getBookmarkState(id: Long): Flowable<Boolean>
}