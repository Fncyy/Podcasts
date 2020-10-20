package hu.bme.aut.android.podcasts.util.extensions

fun String.toSortBy(): Int {
    return when (this) {
        DATE -> 1
        RELEVANCE -> 0
        else -> 0
    }
}

const val DATE = "Date"
const val RELEVANCE = "Relevance"