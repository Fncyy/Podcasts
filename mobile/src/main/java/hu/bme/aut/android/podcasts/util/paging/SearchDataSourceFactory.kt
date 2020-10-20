package hu.bme.aut.android.podcasts.util.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import hu.bme.aut.android.podcasts.domain.model.Podcast
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import javax.inject.Inject

class SearchDataSourceFactory @Inject constructor(
    private val podcastInteractor: PodcastInteractor
) : DataSource.Factory<Int, Podcast>() {

    val sourceLiveData = MutableLiveData<SearchDataSource>()
    private lateinit var query: String
    private var language: Language? = null
    private var region: Region? = null
    private var sortBy: String? = null

    override fun create(): DataSource<Int, Podcast> {
        val source = SearchDataSource(podcastInteractor, query, language, region, sortBy)
        sourceLiveData.postValue(source)
        return source
    }

    fun init(
        query: String,
        language: Language?,
        region: Region?,
        sortBy: String?
    ) {
        this.query = query
        this.language = language
        this.region = region
        this.sortBy = sortBy
    }
}