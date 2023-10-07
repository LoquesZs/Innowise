package by.beltelecom.innowise.domain.repository

import androidx.paging.PagingData
import by.beltelecom.innowise.domain.entities.Photo
import io.reactivex.Flowable
import io.reactivex.Single

interface BookmarksRepository {

    fun getBookmarks(): Flowable<PagingData<Photo>>
}