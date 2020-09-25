package hu.bme.aut.android.podcasts.ui.details

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val detailsPresenter: DetailsPresenter
) : RainbowCakeViewModel<DetailsViewState>(Initial) {

    fun load() = execute {
        viewState = DetailsReady(detailsPresenter.getData())
    }

}
