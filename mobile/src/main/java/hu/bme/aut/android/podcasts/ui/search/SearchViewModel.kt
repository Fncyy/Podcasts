package hu.bme.aut.android.podcasts.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.podcasts.domain.model.Podcast
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.util.paging.Listing
import hu.bme.aut.android.podcasts.util.paging.SearchRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchPresenter: SearchPresenter,
    private val searchRepository: SearchRepository
) : RainbowCakeViewModel<SearchViewState>(Loading) {

    private val podcastsList = MutableLiveData<Listing<Podcast>>()
    val podcasts = Transformations.switchMap(podcastsList) { it.pagedList }
    var networkState = Transformations.switchMap(podcastsList) { it.networkState }

    fun loadUserData() = execute {
        viewState = Loading
        viewState = Initialized(
            searchPresenter.loadUserData(),
            searchPresenter.getAvailableRegions(),
            searchPresenter.getAvailableLanguages()
        )
    }

    fun getPagedPodcasts(query: String, language: Language?, region: Region?, sortBy: String?) =
        execute {
            podcastsList.value = searchRepository.getPagedPodcasts(query, language, region, sortBy)
        }

    fun updateStarred(user: FirebaseUser?, id: String, starred: Boolean) = execute {
        val uid = user?.uid ?: ""
        searchPresenter.updateStarred(uid, id, starred)
    }

    fun retry() {
        val listing = podcastsList.value
        listing?.retry?.invoke()
    }
}
