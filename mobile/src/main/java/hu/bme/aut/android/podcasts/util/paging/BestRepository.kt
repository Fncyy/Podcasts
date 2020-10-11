package hu.bme.aut.android.podcasts.util.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.paging.PagedList
import androidx.paging.toLiveData
import hu.bme.aut.android.podcasts.domain.Podcast
import javax.inject.Inject

class BestRepository @Inject constructor(
    private val bestDataSourceFactory: BestDataSourceFactory
) {
    fun getPagedPodcasts(): Listing<Podcast> {
        val livePagedList =
            bestDataSourceFactory.toLiveData(20)
        return Listing(
            pagedList = livePagedList,
            networkState = switchMap(bestDataSourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = { bestDataSourceFactory.sourceLiveData.value?.retryAllFailed() }
        )
    }
}

data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val retry: () -> Unit
)