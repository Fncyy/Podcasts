package hu.bme.aut.android.podcasts.ui.menu

import android.util.Log
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.podcasts.domain.Language
import hu.bme.aut.android.podcasts.domain.Region
import hu.bme.aut.android.podcasts.domain.UserData
import hu.bme.aut.android.podcasts.util.FirebaseDatabaseAccessor
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    private val menuPresenter: MenuPresenter
) : RainbowCakeViewModel<MenuViewState>(Loading),
    FirebaseDatabaseAccessor.FirebaseDatabaseInsertionListener {

    class NewRegionEvent(val region: Region) : OneShotEvent
    class NewLanguageEvent(val language: Language) : OneShotEvent
    class NewExplicitEvent(val explicit: Boolean) : OneShotEvent

    fun load(user: FirebaseUser?) = execute {
        viewState = Loading
        viewState = if (user == null) {
            val userId = ""
            LoggedOut(
                menuPresenter.getUserData(userId, "Podcasts", this),
                menuPresenter.getAvailableRegions(),
                menuPresenter.getAvailableLanguages()
            )
        } else {
            val userId = user.uid
            LoggedIn(
                menuPresenter.getUserData(userId, user.displayName ?: "Podcasts", this),
                menuPresenter.getAvailableRegions(),
                menuPresenter.getAvailableLanguages()
            )
        }
    }

    fun updateSettings(user: FirebaseUser?, data: UserData) = execute {
        Log.d("upload", user?.uid ?: "empty")
        menuPresenter.updateSettings(user?.uid ?: "", data)
    }

    fun updateExplicitContent(id: String, explicit: Boolean) = execute {
        menuPresenter.updateExplicitContent(id, explicit)
    }

    override fun onRegionAdded(region: Region) {
        postEvent(NewRegionEvent(region))
    }

    override fun onLanguageAdded(language: Language) {
        postEvent(NewLanguageEvent(language))
    }

    override fun onExplicitChanged(explicit: Boolean) {
        postEvent((NewExplicitEvent(explicit)))
    }
}
