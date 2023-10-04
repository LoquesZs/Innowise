package by.beltelecom.innowise.common.network

import by.beltelecom.innowise.common.network.models.CommonPhotoResponse
import by.beltelecom.innowise.common.network.models.FeaturedCollections
import by.beltelecom.innowise.common.network.models.PhotoModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PexelsApiService {

    @GET("search")
    fun search(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30
    ): Single<CommonPhotoResponse>

    @GET("curated")
    fun popular(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30
    ): Single<CommonPhotoResponse>

    @GET("collections/featured")
    fun featuredCollections(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 7
    ): Single<FeaturedCollections>

    @GET("photos/{id}")
    fun getPhotoByID(
        @Path("id") id: Long
    ): Single<PhotoModel>
}
