package hu.bme.aut.android.podcasts.domain

import hu.bme.aut.android.podcasts.data.disk.DiskDataSource
import hu.bme.aut.android.podcasts.data.network.FirebaseDatabaseAccessor
import hu.bme.aut.android.podcasts.data.network.FirebaseDatabaseAccessor.FirebaseDatabaseInsertionListener
import hu.bme.aut.android.podcasts.shared.data.disk.SharedPreferencesProvider
import hu.bme.aut.android.podcasts.shared.domain.model.UserData
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val diskDataSource: DiskDataSource,
    private val firebaseDatabaseAccessor: FirebaseDatabaseAccessor,
    private val sharedPreferencesProvider: SharedPreferencesProvider
) {

    fun getUserData(
        id: String = "",
        displayName: String = "",
        listener: FirebaseDatabaseInsertionListener? = null
    ): UserData {
        return if (id.isEmpty()) {
            diskDataSource.getUserData(displayName)
        } else {
            firebaseDatabaseAccessor.getUserData(id, displayName, listener!!)
        }
    }

    fun updateUserData(id: String, data: UserData) {
        diskDataSource.updateUserData(data)
        if (id.isNotEmpty())
            firebaseDatabaseAccessor.updateUserData(id, data)
    }

    fun migrateData(id: String) {
        firebaseDatabaseAccessor.updateUserData(id, diskDataSource.getUserData(""))
    }

    fun initializeFavourites(
        id: String,
        listener: FirebaseDatabaseInsertionListener
    ): List<String> {
        return if (id.isEmpty())
            sharedPreferencesProvider.getFavourites()
        else
            firebaseDatabaseAccessor.getFavourites(id, listener)
    }
}