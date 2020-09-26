package hu.bme.aut.android.podcasts.domain

import hu.bme.aut.android.podcasts.data.network.NetworkDataSource
import javax.inject.Inject

class PodcastInteractor @Inject constructor(
    private val networkDataSource: NetworkDataSource
) {

    suspend fun getBestPodcasts(genreId: String?, page: Int?, safeMode: Int?): SearchResult =
        networkDataSource.getBestPodcasts(genreId, page, safeMode)

    suspend fun getGenres(): GenresResult =
        networkDataSource.getGenres()
}