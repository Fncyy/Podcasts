package hu.bme.aut.android.podcasts.data.disk

import hu.bme.aut.android.podcasts.domain.Podcast
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiskDataSource @Inject constructor(
    private val bestPodcastDao: BestPodcastDao,
    private val searchPodcastDao: SearchPodcastDao
) {

    suspend fun getPodcastById(id: String): Podcast? {
        return bestPodcastDao.getPodcastById(id)?.toPodcast()
            ?: searchPodcastDao.getPodcastById(id)?.toPodcast()
    }

    suspend fun insertAllBestPodcasts(podcasts: List<Podcast>) {
        bestPodcastDao.insertAllPodcasts(podcasts.map(Podcast::toRoomBestPodcastItem))
    }

    suspend fun insertAllSearchPodcasts(podcasts: List<Podcast>) {
        searchPodcastDao.insertAllPodcasts(podcasts.map(Podcast::toRoomSearchPodcastItem))
    }

    suspend fun removeAllBestPodcasts() {
        bestPodcastDao.removeAllPodcasts()
    }

    suspend fun removeAllSearchPodcasts() {
        searchPodcastDao.removeAllPodcasts()
    }

    suspend fun updateFavourite(id: String, starred: Boolean) {
        bestPodcastDao.updateFavourite(id, starred)
        searchPodcastDao.updateFavourite(id, starred)
    }

}