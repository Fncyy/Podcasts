package hu.bme.aut.android.podcasts.domain.model

data class SearchResult(
    val nextOffset: Int,
    val podcasts: List<Podcast>,
    val total: Int
)