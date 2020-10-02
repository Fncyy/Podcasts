package hu.bme.aut.android.podcasts.ui.home

import android.util.Log
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homePresenter: HomePresenter
) : RainbowCakeViewModel<HomeViewState>(Loading) {

    fun load() = execute {
        viewState = Loading
        val result = homePresenter.getBestPodcasts()
        Log.d("result", result.toString())
        viewState = HomeReady(result)
    }

    fun updateStarred(user: FirebaseUser?, id: String, starred: Boolean) = execute {
        val uid = user?.uid ?: ""
        homePresenter.updateStarred(uid, id, starred)
    }

}
