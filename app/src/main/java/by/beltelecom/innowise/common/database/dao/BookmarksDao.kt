package by.beltelecom.innowise.common.database.dao

import androidx.paging.rxjava2.RxPagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.beltelecom.innowise.common.database.entities.PhotoEntity
import io.reactivex.Completable

@Dao
interface BookmarksDao {

    @Query("SELECT * FROM ${PhotoEntity.TABLE_NAME}")
    fun getBookmarks(): RxPagingSource<Int, PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBookmark(photoEntity: PhotoEntity): Completable

    @Delete
    fun removeBookmark(photoEntity: PhotoEntity): Completable
}