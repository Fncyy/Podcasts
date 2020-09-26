package hu.bme.aut.android.podcasts.data.network.model

data class GenresResponse(
    val genres: List<Genre>
)

data class Genre(
    val id: Int,
    val name: String,
    val parent_id: Int
)