package hu.bme.aut.android.podcasts.shared.domain.model

data class UserData(
    val displayName: String,
    val explicitContent: Boolean?,
    val region: Region?,
    val language: Language?
)

data class Region(
    val key: String = "",
    val name: String = ""
)

data class Language(
    val name: String = ""
)