package by.beltelecom.innowise.common.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import by.beltelecom.innowise.common.database.entities.PhotoEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface BookmarksDao {

    @Query("SELECT * FROM ${PhotoEntity.TABLE_NAME}")
    fun getBookmarks(): Flowable<List<PhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBookmark(photoEntity: PhotoEntity): Completable

    @Delete
    fun removeBookmark(photoEntity: PhotoEntity): Completable
}