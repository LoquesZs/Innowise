package by.beltelecom.innowise.di

import by.beltelecom.innowise.data.BookmarksRepositoryImpl
import by.beltelecom.innowise.domain.repository.BookmarksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface BookmarksModule {

    @Binds
    fun bindBookmarksRepository(bookmarksRepositoryImpl: BookmarksRepositoryImpl): BookmarksRepository
}