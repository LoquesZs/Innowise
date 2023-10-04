package by.beltelecom.innowise.common.database.di

import android.content.Context
import androidx.room.Room
import by.beltelecom.innowise.common.database.PhotoDatabase
import by.beltelecom.innowise.common.database.dao.BookmarksDao
import by.beltelecom.innowise.common.database.dao.DetailDao
import by.beltelecom.innowise.common.database.dao.HomeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PhotoDatabase {
        return Room.databaseBuilder(
            context,
            PhotoDatabase::class.java,
            PhotoDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideHomeDao(db: PhotoDatabase): HomeDao = db.homeDao

    @Provides
    fun provideBookmarksDao(db: PhotoDatabase): BookmarksDao = db.bookmarksDao

    @Provides
    fun provideDetailDao(db: PhotoDatabase): DetailDao = db.detailDao
}