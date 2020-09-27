package hu.bme.aut.android.podcasts.domain

import dagger.Lazy
import hu.bme.aut.android.podcasts.data.disk.DiskDataSource
import hu.bme.aut.android.podcasts.data.network.NetworkDataSource
import hu.bme.aut.android.podcasts.util.FavouriteDecoder
import javax.inject.Inject

class PodcastInteractor @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val diskDataSource: DiskDataSource,
    private val favouriteDecoder: Lazy<FavouriteDecoder>
) {

    suspend fun getBestPodcasts(genreId: String?, page: Int?, safeMode: Int?): SearchResult {
        val result = networkDataSource.getBestPodcasts(genreId, page, safeMode)
        diskDataSource.removeAllBestPodcasts()
        diskDataSource.insertAllBestPodcasts(result.podcasts)
        return result
    }

    // TODO search with api then save with room

    suspend fun getBestPodcastById(id: String): Podcast? {
        return diskDataSource.getPodcastById(id)
    }

    suspend fun getGenres(): GenresResult {
        return networkDataSource.getGenres()
    }

    suspend fun getFavourites(): List<String> {
        return listOf(
            "d50f00edeb8c446f955f80716154a3a3",
            "b83b5ecc70aa4d5294836981f2716a95"
        )
        // TODO return proper values
    }

    suspend fun updateFavourites(id: String, starred: Boolean) {
        favouriteDecoder.get().updateStarred(id, starred)
        // TODO save to firebase
        diskDataSource.updateFavourite(id, starred)
    }
}