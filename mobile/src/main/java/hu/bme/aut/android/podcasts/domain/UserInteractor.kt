package hu.bme.aut.android.podcasts.domain

import hu.bme.aut.android.podcasts.data.disk.DiskDataSource
import hu.bme.aut.android.podcasts.shared.domain.model.UserData
import hu.bme.aut.android.podcasts.shared.util.SharedPreferencesProvider
import hu.bme.aut.android.podcasts.util.FirebaseDatabaseAccessor
import hu.bme.aut.android.podcasts.util.FirebaseDatabaseAccessor.FirebaseDatabaseInsertionListener
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val diskDataSource: DiskDataSource,
    private val firebaseDatabaseAccessor: FirebaseDatabaseAccessor,
    private val sharedPreferencesProvider: SharedPreferencesProvider
) {

    suspend fun getUserData(
        id: String,
        displayName: String,
        listener: FirebaseDatabaseInsertionListener
    ): UserData {
        return if (id.isEmpty()) {
            diskDataSource.getUserData(displayName)
        } else {
            firebaseDatabaseAccessor.getUserData(id, displayName, listener)
        }
    }

    suspend fun updateUserData(id: String, data: UserData) {
        diskDataSource.updateUserData(data)
        if (id.isNotEmpty())
            firebaseDatabaseAccessor.updateUserData(id, data)
    }

    suspend fun migrateData(id: String) {
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