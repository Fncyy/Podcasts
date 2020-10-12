package hu.bme.aut.android.podcasts.data.network.model

data class SearchResponse(
    val count: Int?,
    val next_offset: Int?,
    val results: List<Result>,
    val took: Double?,
    val total: Int?
)