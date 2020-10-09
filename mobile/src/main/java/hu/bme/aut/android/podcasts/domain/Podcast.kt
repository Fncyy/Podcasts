package hu.bme.aut.android.podcasts.domain

data class Podcast(
    val explicitContent: Boolean,
    val genres: String,
    val id: String,
    val publisher: String,
    val thumbnail: String,
    val title: String,
    var starred: Boolean
)