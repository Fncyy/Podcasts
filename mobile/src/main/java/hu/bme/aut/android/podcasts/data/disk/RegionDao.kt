package hu.bme.aut.android.podcasts.data.disk

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import hu.bme.aut.android.podcasts.data.disk.entities.REGION_TABLE_NAME
import hu.bme.aut.android.podcasts.data.disk.entities.RoomRegionItem

@Dao
interface RegionDao {

    @Query("SELECT * FROM $REGION_TABLE_NAME")
    suspend fun getRegions(): List<RoomRegionItem>

    @Insert(onConflict = IGNORE)
    suspend fun insertRegion(roomRegionItem: RoomRegionItem)

    @Transaction
    suspend fun insertAllRegions(list: List<RoomRegionItem>) {
        deleteAll()
        list.forEach { insertRegion(it) }
    }

    @Query("DELETE FROM $REGION_TABLE_NAME WHERE `key` = :key")
    suspend fun deleteRegion(key: String)

    @Query("DELETE FROM $REGION_TABLE_NAME")
    suspend fun deleteAll()

}