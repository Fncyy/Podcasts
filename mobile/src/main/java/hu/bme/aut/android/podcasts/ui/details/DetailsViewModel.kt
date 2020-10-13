package hu.bme.aut.android.podcasts.ui.details

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val detailsPresenter: DetailsPresenter
) : RainbowCakeViewModel<DetailsViewState>(Initial) {

    fun load(id: String) = execute {
        detailsPresenter.getPodcast(id)?.let {
            viewState = DetailsReady(it)
        }
    }

    fun updateStarred(user: FirebaseUser?, id: String, starred: Boolean) = execute {
        val uid = user?.uid ?: ""
        detailsPresenter.updateStarred(uid, id, starred)
    }

}
