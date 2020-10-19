package hu.bme.aut.android.podcasts.util

import com.google.firebase.database.*
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.shared.domain.model.UserData
import hu.bme.aut.android.podcasts.shared.util.SharedPreferencesProvider
import javax.inject.Inject

class FirebaseDatabaseAccessor @Inject constructor(
    private val sharedPreferencesProvider: SharedPreferencesProvider
) {

    private companion object {
        private const val EXPLICIT = "explicit"
        private const val FAVOURITES = "favourites"
        private const val REGION = "region"
        private const val LANGUAGE = "language"
    }

    private val instance = FirebaseDatabase.getInstance()

    private val listeners: MutableList<FirebaseDatabaseInsertionListener> = mutableListOf()

    private var userId = ""
    private var favouritesListeners = mutableListOf<ChildEventListener>()

    fun getFavourites(id: String, listener: FirebaseDatabaseInsertionListener): List<String> {
        if (!listeners.contains(listener))
            listeners.add(listener)
        if (id != userId) {
            val reference = instance.reference.child(id).child(FAVOURITES)
            val childEventListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val podcastId = snapshot.getValue(String::class.java)
                    if (podcastId != null)
                        listeners.forEach { it.onFavouriteAdded(podcastId) }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            }
            favouritesListeners.run {
                forEach {
                    reference.removeEventListener(it)
                }
                clear()
                add(childEventListener)
            }
            reference
                .addChildEventListener(childEventListener)
        }
        return emptyList()
    }

    fun updateFavourites(id: String, list: List<String>) {
        val truncatedList = list.toMutableList().apply { remove("") }
        instance.reference.child(id).child(FAVOURITES).run {
            if (truncatedList.isNotEmpty())
                setValue(truncatedList)
            else
                removeValue()
        }
    }

    fun getUserData(
        id: String,
        displayName: String,
        listener: FirebaseDatabaseInsertionListener
    ): UserData {
        getExplicitContent(id, listener)
        getRegion(id, listener)
        getLanguage(id, listener)
        return UserData(
            displayName = displayName,
            explicitContent = null,
            region = null,
            language = null
        )
    }

    fun updateUserData(id: String, data: UserData) {
        updateExplicitContent(id, data.explicitContent!!)
        updateRegion(id, data.region!!)
        updateLanguage(id, data.language!!)
    }

    private fun getExplicitContent(
        id: String,
        listener: FirebaseDatabaseInsertionListener
    ): Boolean {
        if (!listeners.contains(listener))
            listeners.add(listener)
        instance.reference.child(id).child(EXPLICIT)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listeners.forEach {
                        val value = snapshot.getValue(Boolean::class.java) ?: true
                        it.onExplicitChanged(value)
                        sharedPreferencesProvider.editExplicitContent(value)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return false
    }

    private fun updateExplicitContent(id: String, explicit: Boolean) {
        instance.reference.child(id).child(EXPLICIT).setValue(explicit)
    }

    private fun getRegion(
        id: String,
        listener: FirebaseDatabaseInsertionListener
    ) {
        if (!listeners.contains(listener))
            listeners.add(listener)
        instance.reference.child(id).child(REGION)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listeners.forEach {
                        val result = (snapshot.getValue(String::class.java) ?: ",").split(",")
                        val value = Region(result.first(), result.last())
                        it.onRegionChanged(value)
                        sharedPreferencesProvider.editRegion(value)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun updateRegion(id: String, region: Region) {
        instance.reference.child(id).child(REGION).setValue("${region.key},${region.name}")
    }

    private fun getLanguage(
        id: String,
        listener: FirebaseDatabaseInsertionListener
    ) {
        if (!listeners.contains(listener))
            listeners.add(listener)
        instance.reference.child(id).child(LANGUAGE)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listeners.forEach {
                        val value = Language(snapshot.getValue(String::class.java) ?: "")
                        it.onLanguageChanged(value)
                        sharedPreferencesProvider.editLanguage(value)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun updateLanguage(id: String, language: Language) {
        instance.reference.child(id).child(LANGUAGE).setValue(language.name)
    }

    interface FirebaseDatabaseInsertionListener {
        fun onExplicitChanged(explicit: Boolean) {}
        fun onFavouriteAdded(id: String) {}
        fun onRegionChanged(region: Region) {}
        fun onLanguageChanged(language: Language) {}
    }
}