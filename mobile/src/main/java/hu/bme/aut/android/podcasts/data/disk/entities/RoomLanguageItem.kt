package hu.bme.aut.android.podcasts.data.disk.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val LANGUAGE_TABLE_NAME = "Languages"

@Entity(tableName = LANGUAGE_TABLE_NAME)
class RoomLanguageItem(
    @PrimaryKey
    val name: String
)