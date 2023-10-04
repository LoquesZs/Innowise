package by.beltelecom.innowise.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import by.beltelecom.innowise.common.database.dao.BookmarksDao
import by.beltelecom.innowise.common.database.dao.DetailDao
import by.beltelecom.innowise.common.database.dao.HomeDao
import by.beltelecom.innowise.common.database.entities.CommonEntity
import by.beltelecom.innowise.common.database.entities.PhotoEntity

@Database(entities = [PhotoEntity::class, CommonEntity::class], version = 1)
abstract class PhotoDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "photos_db"
    }

    abstract val homeDao: HomeDao

    abstract val detailDao: DetailDao

    abstract val bookmarksDao: BookmarksDao
}