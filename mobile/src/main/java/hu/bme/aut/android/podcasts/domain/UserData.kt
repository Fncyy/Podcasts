package hu.bme.aut.android.podcasts.domain

data class UserData(
    val displayName: String,
    val explicitContent: Boolean?,
    val regions: MutableList<Region>?,
    val languages: MutableList<Language>?
)

data class Region(
    val key: String = "",
    val name: String = ""
)

data class Language(
    val name: String = ""
)