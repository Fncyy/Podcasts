package hu.bme.aut.android.podcasts.util.paging

import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import hu.bme.aut.android.podcasts.domain.model.Podcast
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val searchDataSourceFactory: SearchDataSourceFactory
) {
    fun getPagedPodcasts(
        query: String,
        language: Language?,
        region: Region?,
        sortBy: String?
    ): Listing<Podcast> {
        searchDataSourceFactory.init(query, language, region, sortBy)
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