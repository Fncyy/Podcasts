package hu.bme.aut.android.podcasts.data.disk

import hu.bme.aut.android.podcasts.data.disk.entities.RoomFavouritePodcastItem
import hu.bme.aut.android.podcasts.domain.model.FullPodcast
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.shared.domain.model.UserData
import hu.bme.aut.android.podcasts.shared.util.SharedPreferencesProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiskDataSource @Inject constructor(
    private val bestPodcastDao: BestPodcastDao,
    private val favouritePodcastDao: FavouritePodcastDao,
    private val languageDao: LanguageDao,
    private val regionDao: RegionDao,
    private val searchPodcastDao: SearchPodcastDao,
    private val sharedPreferencesProvider: SharedPreferencesProvider
) {

    suspend fun getUserData(displayName: String) =
        sharedPreferencesProvider.getUserData(displayName)

    suspend fun updateUserData(userData: UserData) {
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

    suspend fun insertAllSearchPodcasts(podcasts: List<FullPodcast>) {
        searchPodcastDao.insertAllPodcasts(podcasts.map(FullPodcast::toRoomSearchPodcastItem))
    }

    suspend fun insertAllFavouritePodcasts(podcasts: List<FullPodcast>) {
        favouritePodcastDao.insertAllPodcasts(podcasts.map(FullPodcast::toRoomFavouritePodcastItem))
    }

    suspend fun removeAllBestPodcasts() {
        bestPodcastDao.removeAllPodcasts()
    }

    suspend fun removeAllSearchPodcasts() {
        searchPodcastDao.removeAllPodcasts()
    }

    suspend fun removeAllFavouritePodcasts() {
        favouritePodcastDao.removeAllPodcasts()
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

    suspend fun getRegions(): List<Region> {
        return regionDao.getRegions().map { Region(it.key, it.name) }
    }

    suspend fun getLanguages(): List<Language> {
        return languageDao.getLanguages().map { Language(it.name) }
    }

}