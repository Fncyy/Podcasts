package hu.bme.aut.android.podcasts.util.extensions

fun Boolean.toInt() = if (this) 1 else 0

fun Boolean.toExplicit() = if (this) 0 else 1