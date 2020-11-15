package hu.bme.aut.android.podcasts.shared.data.network.model

data class Result(
    val description_highlighted: String?,
    val description_original: String?,
    val earliest_pub_date_ms: Long?,
    val email: String?,
    val explicit_content: Boolean?,
    val genre_ids: List<Int>,
    val id: String?,
    val image: String?,
    val itunes_id: Int?,
    val latest_pub_date_ms: Long?,
    val listennotes_url: String?,
    val publisher_highlighted: String?,
    val publisher_original: String?,
    val rss: String?,
    val thumbnail: String?,
    val title_highlighted: String?,
    val title_original: String?,
    val total_episodes: Int?,
    val website: String?
)