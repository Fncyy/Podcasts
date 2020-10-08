package hu.bme.aut.android.podcasts.util

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import hu.bme.aut.android.podcasts.domain.Podcast
import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import javax.inject.Inject

class BestPodcastsDataSourceFactory @Inject constructor(
    private val podcastInteractor: PodcastInteractor
) : DataSource.Factory<Int, Podcast>() {
    val sourceLiveData = MutableLiveData<BestPodcastDataSource>()
    override fun create(): DataSource<Int, Podcast> {
        val source = BestPodcastDataSource(podcastInteractor)
        sourceLiveData.postValue(source)
        return source
    }
}