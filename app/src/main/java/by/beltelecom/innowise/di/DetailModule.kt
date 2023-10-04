package by.beltelecom.innowise.di

import by.beltelecom.innowise.data.DetailRepositoryImpl
import by.beltelecom.innowise.domain.repository.DetailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface DetailModule {

    @Binds
    @ViewModelScoped
    fun bindDetailRepository(repository: DetailRepositoryImpl): DetailRepository
}