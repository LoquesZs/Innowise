package by.beltelecom.innowise.common.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import by.beltelecom.innowise.common.database.dao.BookmarksDao
import by.beltelecom.innowise.common.database.dao.DetailDao
import by.beltelecom.innowise.common.database.entities.PhotoEntity

@Database(
    entities = [PhotoEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class PhotoDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "photos_db"
    }

    abstract val detailDao: DetailDao

    abstract val bookmarksDao: BookmarksDao
}