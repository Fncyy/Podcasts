package hu.bme.aut.android.podcasts.domain

data class SearchResult(
    val nextOffset: Int,
    val podcasts: List<Podcast>,
    val total: Int
)