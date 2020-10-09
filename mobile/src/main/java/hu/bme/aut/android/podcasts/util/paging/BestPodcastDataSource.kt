package hu.bme.aut.android.podcasts.util.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import hu.bme.aut.android.podcasts.ui.home.HomePresenter.Podcast
import hu.bme.aut.android.podcasts.ui.home.toBestPodcasts
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
            val result = podcastInteractor.getBestPodcasts(null, null, null).toBestPodcasts()
            val next = if (result.hasNext) result.nextPageNumber else null
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(result.podcasts, null, next)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Podcast>) {
        // Won't be loading data backwards
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Podcast>) {
        networkState.postValue(NetworkState.LOADING)

        CoroutineScope(Dispatchers.IO).async {
            val result = podcastInteractor.getBestPodcasts(null, params.key, null).toBestPodcasts()
            val next = if (result.hasNext) result.nextPageNumber else null
            networkState.postValue(NetworkState.LOADED)
            callback.onResult(result.podcasts, next)
        }
    }
}