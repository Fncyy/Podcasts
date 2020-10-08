package hu.bme.aut.android.podcasts.data.network

import dagger.Lazy
import hu.bme.aut.android.podcasts.domain.GenresResult
import hu.bme.aut.android.podcasts.domain.Language
import hu.bme.aut.android.podcasts.domain.Region
import hu.bme.aut.android.podcasts.domain.SearchResult
import hu.bme.aut.android.podcasts.util.FavouriteDecoder
import hu.bme.aut.android.podcasts.util.GenreDecoder
import hu.bme.aut.android.podcasts.util.extensions.toInt
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkDataSource @Inject constructor(
    private val listenNotesAPI: ListenNotesAPI,
    private val genreDecoder: Lazy<GenreDecoder>,
    private val favouriteDecoder: Lazy<FavouriteDecoder>
) {
    suspend fun getBestPodcasts(genreId: String?, page: Int?, safeMode: Int?): SearchResult =
        listenNotesAPI.getBestPodcasts(
            genreId = genreId ?: "0",
            page = page ?: 1,
            safeMode = safeMode ?: 0
        ).toSearchResult(genreDecoder, favouriteDecoder)

    suspend fun getGenres(topLevelOnly: Boolean = false): GenresResult =
        listenNotesAPI.getGenres(topLevelOnly = topLevelOnly.toInt()).toGenresResult()

    suspend fun getAvailableRegions(): List<Region> =
        listenNotesAPI.getRegions().regions.map { Region(it.key, it.value) }

    suspend fun getAvailableLanguages(): List<Language> =
        listenNotesAPI.getLanguages().languages.map { Language(it) }
}