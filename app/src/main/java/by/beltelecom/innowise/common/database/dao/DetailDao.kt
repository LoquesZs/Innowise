package by.beltelecom.innowise.common.database.dao

import androidx.room.Dao
import androidx.room.Query
import by.beltelecom.innowise.common.database.entities.PhotoEntity
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface DetailDao {

    @Query("SELECT * FROM ${PhotoEntity.TABLE_NAME} WHERE ${PhotoEntity.COLUMN_ID} = :id")
    fun getPhoto(id: Long): Single<PhotoEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM ${PhotoEntity.TABLE_NAME} WHERE ${PhotoEntity.COLUMN_ID} = :id)")
    fun getBookmarkState(id: Long): Flowable<Boolean>
}