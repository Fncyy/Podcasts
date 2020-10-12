package hu.bme.aut.android.podcasts.util.paging

import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import hu.bme.aut.android.podcasts.domain.Podcast
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchDataSourceFactory: SearchDataSourceFactory
) {
    fun getPagedPodcasts(query: String): Listing<Podcast> {
        searchDataSourceFactory.init(query)
        val livePagedList =
            searchDataSourceFactory.toLiveData(10)
        return Listing(
            pagedList = livePagedList,
            networkState = Transformations.switchMap(searchDataSourceFactory.sourceLiveData) {
                it.networkState
            },
            retry = { searchDataSourceFactory.sourceLiveData.value?.retryAllFailed() }
        )
    }
}