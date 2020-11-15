package hu.bme.aut.android.podcasts.util.paging

import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import hu.bme.aut.android.podcasts.domain.model.Podcast
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class SearchDataSource(
    private val podcastInteractor: PodcastInteractor,
    private val query: String,
    private val language: Language?,
    private val region: Region?,
    private val sortBy: String?
) : PodcastDataSource() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Podcast>
    ) {
        networkState.postValue(NetworkState.LOADING)

        CoroutineScope(Dispatchers.IO).async {
            try {
                podcastInteractor.removeAllSearchPodcasts()
                val result =
                    podcastInteractor.getSearchResult(query, null, language, region, sortBy)
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(result.podcasts, null, result.nextOffset)
            } catch (e: Exception) {
                retry = { loadInitial(params, callback) }
                val error = NetworkState.error("The server could not be reached.")
                networkState.postValue(error)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Podcast>) {
        // Won't be loading data backwards
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Podcast>) {
        networkState.postValue(NetworkState.LOADING)

        CoroutineScope(Dispatchers.IO).async {
            try {
                val result =
                    podcastInteractor.getSearchResult(query, params.key, language, region, sortBy)
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(result.podcasts, result.nextOffset)
            } catch (e: Exception) {
                retry = { loadAfter(params, callback) }
                networkState.postValue(NetworkState.error("The server could not be reached."))
            }
        }
    }
}