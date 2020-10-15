package hu.bme.aut.android.podcasts.shared.data.network

import hu.bme.aut.android.podcasts.shared.data.network.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ListenNotesAPI {

    @GET("best_podcasts")
    suspend fun getBestPodcasts(
        @Query("genre_id") genreId: String,
        @Query("page") page: Int,
        @Query("safe_mode") safeMode: Int
    ): BestPodcastsResponse

    @GET("genres")
    suspend fun getGenres(
        @Query("top_level_only") topLevelOnly: Int
    ): GenresResponse

    @GET("regions")
    suspend fun getRegions(): RegionsWrapper

    @GET("languages")
    suspend fun getLanguages(): LanguagesWrapper

    @GET("podcasts/{id}")
    suspend fun getPodcast(
        @Path("id") id: String
    ): IndividualPodcast

    @GET("search")
    suspend fun getSearchResult(
        @Query("q") query: String,
        @Query("offset") offset: Int?,
        @Query("safe_mode") safeMode: Int?,
        @Query("type") type: String = "podcast"
    ): SearchResponse
}