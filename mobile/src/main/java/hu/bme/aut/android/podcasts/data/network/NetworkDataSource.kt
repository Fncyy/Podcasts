package hu.bme.aut.android.podcasts.data.network

import dagger.Lazy
import hu.bme.aut.android.podcasts.domain.model.BestPodcastResult
import hu.bme.aut.android.podcasts.domain.model.FullPodcast
import hu.bme.aut.android.podcasts.domain.model.GenresResult
import hu.bme.aut.android.podcasts.domain.model.SearchResult
import hu.bme.aut.android.podcasts.shared.data.network.ListenNotesAPI
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
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
    suspend fun getBestPodcasts(
        genreId: String?,
        page: Int?,
        safeMode: Int?,
        region: Region?
    ): BestPodcastResult =
        listenNotesAPI.getBestPodcasts(
            genreId = genreId ?: "0",
            page = page ?: 1,
            safeMode = safeMode,
            region = region?.key
        ).toBestPodcastResult(genreDecoder, favouriteDecoder)

    suspend fun getGenres(topLevelOnly: Boolean = false): GenresResult =
        listenNotesAPI.getGenres(topLevelOnly = topLevelOnly.toInt()).toGenresResult()

    suspend fun getAvailableRegions(): List<Region> =
        listenNotesAPI.getRegions().regions.map { Region(it.key, it.value) }

    suspend fun getAvailableLanguages(): List<Language> =
        listenNotesAPI.getLanguages().languages.map { Language(it) }

    suspend fun getPodcast(id: String): FullPodcast {
        return listenNotesAPI.getPodcast(id).toFullPodcast(genreDecoder, favouriteDecoder)
    }

    suspend fun getSearchResult(
        query: String,
        offset: Int?,
        safeMode: Int?,
        language: Language?,
        region: Region?,
        sortBy: Int?
    ): SearchResult {
        return listenNotesAPI.getSearchResult(
            query,
            offset,
            safeMode,
            language?.name,
            region?.key,
            sortBy
        ).toSearchResult(genreDecoder, favouriteDecoder)
    }
}