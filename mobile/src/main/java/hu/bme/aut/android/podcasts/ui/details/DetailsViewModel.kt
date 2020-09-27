package hu.bme.aut.android.podcasts.ui.details

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val detailsPresenter: DetailsPresenter
) : RainbowCakeViewModel<DetailsViewState>(Initial) {

    fun load(id: String) = execute {
        detailsPresenter.getPodcast(id)?.let {
            viewState = DetailsReady(it)
        }
    }

    fun updateStarred(id: String, starred: Boolean) = execute {
        detailsPresenter.updateStarred(id, starred)
    }

}
