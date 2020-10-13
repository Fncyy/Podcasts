package hu.bme.aut.android.podcasts.data.disk.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val REGION_TABLE_NAME = "Regions"

@Entity(tableName = REGION_TABLE_NAME)
class RoomRegionItem(
    @PrimaryKey
    val key: String,
    val name: String
)