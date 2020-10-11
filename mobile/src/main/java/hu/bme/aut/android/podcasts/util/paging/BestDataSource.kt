package hu.bme.aut.android.podcasts.util.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import hu.bme.aut.android.podcasts.domain.Podcast
import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import hu.bme.aut.android.podcasts.ui.home.toBestPodcasts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class BestDataSource(
    private val podcastInteractor: PodcastInteractor
) :
    PageKeyedDataSource<Int, Podcast>() {

    private var retry: (() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            CoroutineScope(Dispatchers.IO).async {
                it.invoke()
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Podcast>
    ) {
        networkState.postValue(NetworkState.LOADING)

        CoroutineScope(Dispatchers.IO).async {
            try {
                podcastInteractor.removeAllBestPodcasts()
                val result = podcastInteractor.getBestPodcasts(null, null, null).toBestPodcasts()
                val next = if (result.hasNext) result.nextPageNumber else null
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(result.podcasts, null, next)
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
                    podcastInteractor.getBestPodcasts(null, params.key, null).toBestPodcasts()
                val next = if (result.hasNext) result.nextPageNumber else null
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(result.podcasts, next)
            } catch (e: Exception) {
                retry = { loadAfter(params, callback) }
                networkState.postValue(NetworkState.error("The server could not be reached."))
            }
        }
    }
}