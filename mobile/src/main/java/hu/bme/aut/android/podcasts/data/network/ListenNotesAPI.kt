package hu.bme.aut.android.podcasts.data.network

import hu.bme.aut.android.podcasts.data.network.model.BestPodcastsResponse
import hu.bme.aut.android.podcasts.data.network.model.GenresResponse
import hu.bme.aut.android.podcasts.data.network.model.LanguagesWrapper
import hu.bme.aut.android.podcasts.data.network.model.RegionsWrapper
import retrofit2.http.GET
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
}