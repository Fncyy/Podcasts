package hu.bme.aut.android.podcasts.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.podcasts.data.network.FirebaseDatabaseAccessor
import hu.bme.aut.android.podcasts.domain.model.Podcast
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.util.paging.BestRepository
import hu.bme.aut.android.podcasts.util.paging.Listing
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homePresenter: HomePresenter,
    private val bestRepository: BestRepository
) : RainbowCakeViewModel<HomeViewState>(Loading),
    FirebaseDatabaseAccessor.FirebaseDatabaseInsertionListener {

    private val podcastsList = MutableLiveData<Listing<Podcast>>()
    val podcasts = switchMap(podcastsList) { it.pagedList }
    var networkState = switchMap(podcastsList) { it.networkState }
    private val updateCompletionMap: ConcurrentMap<String, Boolean> = ConcurrentHashMap()


    init {
        updateCompletionMap[EXPLICIT] = false
        updateCompletionMap[LANGUAGE] = false
        updateCompletionMap[REGION] = false
        getUserData()
    }

    fun updateStarred(user: FirebaseUser?, id: String, starred: Boolean) = execute {
        val uid = user?.uid ?: ""
        homePresenter.updateStarred(uid, id, starred)
    }

    fun retry() {
        val listing = podcastsList.value
        listing?.retry?.invoke()
    }

    private fun getUserData() = execute {
        val id = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val result = homePresenter.getUserData(id, this)
        if (result.explicitContent != null) loadData()
    }

    private fun loadData() {
        podcastsList.value = bestRepository.getPagedPodcasts()
        viewState = HomeReady
    }

    private fun updateCompleted() = updateCompletionMap.all { it.value == true }

    override fun onExplicitChanged(explicit: Boolean) {
        updateCompletionMap[EXPLICIT] = true
        if (updateCompleted()) loadData()
    }

    override fun onLanguageChanged(language: Language) {
        updateCompletionMap[LANGUAGE] = true
        if (updateCompleted()) loadData()
    }

    override fun onRegionChanged(region: Region) {
        updateCompletionMap[REGION] = true
        if (updateCompleted()) loadData()
    }

    private companion object {
        private const val EXPLICIT = "explicit"
        private const val LANGUAGE = "language"
        private const val REGION = "region"
    }
}
