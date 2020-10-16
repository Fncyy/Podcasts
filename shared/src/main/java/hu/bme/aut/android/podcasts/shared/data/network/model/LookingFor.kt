package hu.bme.aut.android.podcasts.shared.data.network.model

data class LookingFor(
    val cohosts: Boolean?,
    val cross_promotion: Boolean?,
    val guests: Boolean?,
    val sponsors: Boolean?
)