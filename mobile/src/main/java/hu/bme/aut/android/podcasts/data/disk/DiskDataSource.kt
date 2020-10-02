package hu.bme.aut.android.podcasts.data.disk

import hu.bme.aut.android.podcasts.data.disk.entities.RoomLanguageItem
import hu.bme.aut.android.podcasts.data.disk.entities.RoomRegionItem
import hu.bme.aut.android.podcasts.domain.Language
import hu.bme.aut.android.podcasts.domain.Podcast
import hu.bme.aut.android.podcasts.domain.Region
import hu.bme.aut.android.podcasts.domain.UserData
import hu.bme.aut.android.podcasts.util.SharedPreferencesProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiskDataSource @Inject constructor(
    private val bestPodcastDao: BestPodcastDao,
    private val searchPodcastDao: SearchPodcastDao,
    private val regionDao: RegionDao,
    private val languageDao: LanguageDao,
    private val sharedPreferencesProvider: SharedPreferencesProvider
) {

    suspend fun getUserData(displayName: String) = UserData(
        displayName = displayName,
        explicitContent = sharedPreferencesProvider.getExplicitContent(),
        languages = languageDao.getLanguages().map { Language(it.name) }.toMutableList(),
        regions = regionDao.getRegions().map { Region(it.key, it.name) }.toMutableList()
    )

    suspend fun updateUserData(userData: UserData) {
        sharedPreferencesProvider.editExplicitContent(userData.explicitContent!!)
        languageDao.insertAllLanguages(userData.languages!!.map { RoomLanguageItem(it.name) })
        regionDao.insertAllRegions(userData.regions!!.map { RoomRegionItem(it.key, it.name) })
    }

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

    suspend fun getRegions(): List<Region> {
        return regionDao.getRegions().map { Region(it.key, it.name) }
    }

    suspend fun getLanguages(): List<Language> {
        return languageDao.getLanguages().map { Language(it.name) }
    }

}