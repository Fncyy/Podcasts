package hu.bme.aut.android.podcasts.util

import com.google.firebase.database.*
import hu.bme.aut.android.podcasts.domain.Language
import hu.bme.aut.android.podcasts.domain.Region
import hu.bme.aut.android.podcasts.domain.UserData
import javax.inject.Singleton

@Singleton
class FirebaseDatabaseAccessor {

    private companion object {
        private const val EXPLICIT = "explicit"
        private const val FAVOURITES = "favourites"
        private const val REGIONS = "regions"
        private const val LANGUAGES = "languages"
    }

    private val instance = FirebaseDatabase.getInstance()

    private val listeners: MutableList<FirebaseDatabaseInsertionListener> = mutableListOf()

    suspend fun getUserData(
        id: String,
        displayName: String,
        listener: FirebaseDatabaseInsertionListener
    ): UserData {
        getExplicitContent(id, listener)
        getRegions(id, listener)
        getLanguages(id, listener)
        return UserData(
            displayName = displayName,
            explicitContent = null,
            regions = null,
            languages = null
        )
    }

    suspend fun updateUserData(id: String, data: UserData) {
        updateExplicitContent(id, data.explicitContent!!)
        updateRegions(id, data.regions!!)
        updateLanguages(id, data.languages!!)
    }

    fun getExplicitContent(id: String, listener: FirebaseDatabaseInsertionListener): Boolean {
        if (!listeners.contains(listener))
            listeners.add(listener)
        instance.reference.child(id).child(EXPLICIT)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listeners.forEach {
                        it.onExplicitChanged(
                            snapshot.getValue(Boolean::class.java) ?: true
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return false
    }

    fun updateExplicitContent(id: String, explicit: Boolean) {
        instance.reference.child(id).child(EXPLICIT).setValue(explicit)
    }

    fun getFavourites(id: String, listener: FirebaseDatabaseInsertionListener): List<String> {
        if (!listeners.contains(listener))
            listeners.add(listener)
        instance.reference.child(id).child(FAVOURITES)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val podcastId = snapshot.getValue(String::class.java)
                    if (podcastId != null)
                        listeners.forEach { it.onFavouriteAdded(podcastId) }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        return emptyList()
    }

    fun updateFavourites(id: String, list: List<String>) {
        instance.reference.child(id).child(FAVOURITES).setValue(list)
    }

    fun getRegions(id: String, listener: FirebaseDatabaseInsertionListener): MutableList<Region> {
        if (!listeners.contains(listener))
            listeners.add(listener)
        instance.reference.child(id).child(REGIONS)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val region = snapshot.getValue(Region::class.java)
                    if (region != null)
                        listeners.forEach { it.onRegionAdded(region) }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        return mutableListOf()
    }

    fun updateRegions(id: String, list: List<Region>) {
        instance.reference.child(id).child(REGIONS).setValue(list)
    }

    fun getLanguages(
        id: String,
        listener: FirebaseDatabaseInsertionListener
    ): MutableList<Language> {
        if (!listeners.contains(listener))
            listeners.add(listener)
        instance.reference.child(id).child(LANGUAGES)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val language = snapshot.getValue(Language::class.java)
                    if (language != null)
                        listeners.forEach { it.onLanguageAdded(language) }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        return mutableListOf()
    }

    fun updateLanguages(id: String, list: List<Language>) {
        instance.reference.child(id).child(LANGUAGES).setValue(list)
    }

    interface FirebaseDatabaseInsertionListener {
        fun onExplicitChanged(explicit: Boolean) {}
        fun onFavouriteAdded(id: String) {}
        fun onRegionAdded(region: Region) {}
        fun onLanguageAdded(language: Language) {}
    }
}