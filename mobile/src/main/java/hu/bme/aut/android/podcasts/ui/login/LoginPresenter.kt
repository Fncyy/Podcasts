package hu.bme.aut.android.podcasts.ui.login

import hu.bme.aut.android.podcasts.domain.UserInteractor
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val userInteractor: UserInteractor
) {

    suspend fun migrateData(uid: String) {
        userInteractor.migrateData(uid)
    }

}
