package by.beltelecom.innowise.common.database.dao

import androidx.room.Dao
import androidx.room.Query
import by.beltelecom.innowise.common.database.entities.CommonEntity
import io.reactivex.Single

@Dao
interface HomeDao {

    @Query("SELECT * FROM ${CommonEntity.TABLE_NAME}")
    fun getCommon(): Single<List<CommonEntity>>
}