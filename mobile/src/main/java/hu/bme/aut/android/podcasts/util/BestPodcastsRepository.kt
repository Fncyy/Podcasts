package hu.bme.aut.android.podcasts.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.PagedList
import androidx.paging.toLiveData
import hu.bme.aut.android.podcasts.domain.Podcast
import javax.inject.Inject

class BestPodcastsRepository @Inject constructor(
    private val bestPodcastsDataSourceFactory: BestPodcastsDataSourceFactory
) {
    fun getPagedPodcasts(): Listing<Podcast> {
        val livePagedList =
            bestPodcastsDataSourceFactory.toLiveData(20)
        return Listing(
            pagedList = livePagedList,
            networkState = switchMap(bestPodcastsDataSourceFactory.sourceLiveData) {
                it.networkState
            }
        )
    }
}

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>
)