package hu.bme.aut.android.podcasts.ui.search

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchPresenter: SearchPresenter
) : RainbowCakeViewModel<SearchViewState>(Initial) {

    fun load() = execute {
        viewState = SearchReady(searchPresenter.getData())
    }

}
