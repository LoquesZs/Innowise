package by.beltelecom.innowise.domain.repository

import by.beltelecom.innowise.domain.entities.Photo
import io.reactivex.Flowable

interface BookmarksRepository {

    fun getBookmarks(): Flowable<List<Photo>>
}