package by.beltelecom.innowise.di

import by.beltelecom.innowise.data.HomeRepositoryImpl
import by.beltelecom.innowise.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface HomeModule {

    @Binds
    @ViewModelScoped
    fun bindHomeRepository(repository: HomeRepositoryImpl): HomeRepository
}