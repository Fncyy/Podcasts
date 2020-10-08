package hu.bme.aut.android.podcasts.util.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import hu.bme.aut.android.podcasts.domain.Podcast
import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class BestPodcastDataSource(private val podcastInteractor: PodcastInteractor) :
    PageKeyedDataSource<Int, Podcast>() {

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Podcast>
    ) {
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        CoroutineScope(Dispatchers.IO).async {
            val result = podcastInteractor.getBestPodcasts(null, null, null)
            val next = if (result.hasNext) result.nextPageNumber else null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(result.podcasts, null, next)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Podcast>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Podcast>) {
        networkState.postValue(NetworkState.LOADING)

        CoroutineScope(Dispatchers.IO).async {
            val result = podcastInteractor.getBestPodcasts(null, params.key, null)
            val next = if (result.hasNext) result.nextPageNumber else null
            networkState.postValue(NetworkState.LOADED)
            callback.onResult(result.podcasts, next)
        }
    }
}