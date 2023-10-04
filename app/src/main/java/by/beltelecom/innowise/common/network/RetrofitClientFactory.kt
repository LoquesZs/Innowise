package by.beltelecom.innowise.common.network

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class RetrofitClientFactory(context: Context) {

    companion object {
        private const val BASE_URL = "https://api.pexels.com/v1/"
        private const val API_KEY = "x6KxgBmLdoDjkYEgQx86eWbnQXtEbAIyKobVDq9QUwhZYEdi7F5sU1M4"

        private const val CACHE_NAME = "http_cache"
        private const val CACHE_MAX_SIZE = 50L * 1024L * 1024L
    }

    private object AuthorizationHeaderInterceptor : Interceptor {

        private const val AUTHORIZATION_HEADER = "Authorization"

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader(AUTHORIZATION_HEADER, API_KEY)
                .build()
            return chain.proceed(request)
        }
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BODY }
        )
        .addInterceptor(AuthorizationHeaderInterceptor)
        .cache(
            Cache(
                directory = File(context.cacheDir, CACHE_NAME),
                maxSize = CACHE_MAX_SIZE
            )
        )

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    fun getRetrofit(): Retrofit = retrofit
}