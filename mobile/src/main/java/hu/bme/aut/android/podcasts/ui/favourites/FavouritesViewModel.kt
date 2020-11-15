package hu.bme.aut.android.podcasts.ui.favourites

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class FavouritesViewModel @Inject constructor(
    private val favouritesPresenter: FavouritesPresenter
) : RainbowCakeViewModel<FavouritesViewState>(Loading) {

    init {
        getFavouritePodcasts()
    }

    private fun getFavouritePodcasts() = execute {
        viewState = Loading
        viewState = FavouritesReady(favouritesPresenter.getFavouritePodcasts())
    }

    fun updateStarred(user: FirebaseUser?, id: String, starred: Boolean) = execute {
        val uid = user?.uid ?: ""
        favouritesPresenter.updateStarred(uid, id, starred)
    }

}
