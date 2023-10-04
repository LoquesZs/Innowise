package by.beltelecom.innowise.common.network.di

import android.content.Context
import by.beltelecom.innowise.common.network.PexelsApiService
import by.beltelecom.innowise.common.network.RetrofitClientFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofitClient(@ApplicationContext context: Context): RetrofitClientFactory {
        return RetrofitClientFactory(context)
    }

    @Provides
    fun providePexelsNetworkDataSource(retrofitClientFactory: RetrofitClientFactory): PexelsApiService {
        return retrofitClientFactory.getRetrofit().create(PexelsApiService::class.java)
    }
}