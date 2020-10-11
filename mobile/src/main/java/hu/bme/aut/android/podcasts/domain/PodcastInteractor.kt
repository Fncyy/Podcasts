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
        diskDataSource.insertAllBestPodcasts(result.podcasts)
        return result
    }

    suspend fun getFavouritePodcasts(): List<Podcast> {
        diskDataSource.removeAllFavouritePodcasts()
        return mutableListOf<FullPodcast>().also { podcasts ->
            favouriteDecoder.get().getFavourites().forEach { id ->
                podcasts.add(networkDataSource.getPodcast(id))
            }
            diskDataSource.insertAllFavouritePodcasts(podcasts)
        }.map(FullPodcast::toPodcast)
    }

    suspend fun removeAllBestPodcasts() {
        diskDataSource.removeAllBestPodcasts()
    }

    suspend fun getBestPodcastById(id: String): FullPodcast? {
        return diskDataSource.getPodcastById(id)
    }

    suspend fun getGenres(): GenresResult {
        return networkDataSource.getGenres()
    }

    suspend fun updateFavourites(uid: String, id: String, starred: Boolean) {
        favouriteDecoder.get().updateStarred(uid, id, starred)
        diskDataSource.updateFavourite(id, starred)
    }

    suspend fun getAvailableRegions(): List<Region> {
        return networkDataSource.getAvailableRegions()
    }

    suspend fun getAvailableLanguages(): List<Language> {
        return networkDataSource.getAvailableLanguages()
    }
}