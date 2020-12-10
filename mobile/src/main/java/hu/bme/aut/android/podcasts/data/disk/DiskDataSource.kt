package hu.bme.aut.android.podcasts.data.disk

import hu.bme.aut.android.podcasts.data.disk.entities.RoomFavouritePodcastItem
import hu.bme.aut.android.podcasts.domain.model.FullPodcast
import hu.bme.aut.android.podcasts.shared.data.disk.SharedPreferencesProvider
import hu.bme.aut.android.podcasts.shared.domain.model.UserData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiskDataSource @Inject constructor(
    private val bestPodcastDao: BestPodcastDao,
    private val favouritePodcastDao: FavouritePodcastDao,
    private val searchPodcastDao: SearchPodcastDao,
    private val sharedPreferencesProvider: SharedPreferencesProvider
) {

    fun getUserData(displayName: String) =
        sharedPreferencesProvider.getUserData(displayName)

    fun updateUserData(userData: UserData) {
        sharedPreferencesProvider.updateUserData(userData)
    }

    suspend fun getPodcastById(id: String): FullPodcast? {
        return bestPodcastDao.getPodcastById(id)?.toPodcast()
            ?: searchPodcastDao.getPodcastById(id)?.toPodcast()
            ?: favouritePodcastDao.getPodcastById(id)?.toPodcast()
    }

    suspend fun insertAllBestPodcasts(podcasts: List<FullPodcast>) {
        bestPodcastDao.insertAllPodcasts(podcasts.map(FullPodcast::toRoomBestPodcastItem))
    }

    suspend fun removeAllBestPodcasts() {
        bestPodcastDao.removeAllPodcasts()
    }

    suspend fun removeAllSearchPodcasts() {
        searchPodcastDao.removeAllPodcasts()
    }

    suspend fun getAllFavouritePodcasts(): List<FullPodcast> {
        return favouritePodcastDao.getPodcasts().map(RoomFavouritePodcastItem::toPodcast)
    }

    suspend fun updateFavourite(id: String, starred: Boolean, podcast: FullPodcast?) {
        bestPodcastDao.updateFavourite(id, starred)
        searchPodcastDao.updateFavourite(id, starred)
        when {
            starred -> if (podcast != null) favouritePodcastDao.insertPodcast(podcast.toRoomFavouritePodcastItem())
            else favouritePodcastDao.removePodcast(id)
        }
    }

    suspend fun insertFavouritePodcast(podcast: FullPodcast) {
        favouritePodcastDao.insertPodcast(podcast.toRoomFavouritePodcastItem())
    }

    suspend fun removeFavouritePodcast(id: String) {
        favouritePodcastDao.removePodcast(id)
    }

}