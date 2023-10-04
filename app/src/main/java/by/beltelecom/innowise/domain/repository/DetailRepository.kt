package by.beltelecom.innowise.domain.repository

import by.beltelecom.innowise.domain.entities.Photo
import io.reactivex.Observable
import io.reactivex.Single

interface DetailRepository {

    fun getPhoto(id: Long): Single<Photo>

    fun addBookmark(photo: Photo): Single<Long>

    fun removeBookmark(photo: Photo): Single<Int>

    fun getBookmarkState(id: Long): Observable<Boolean>
}