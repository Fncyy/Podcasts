package hu.bme.aut.android.podcasts.data.disk

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import hu.bme.aut.android.podcasts.data.disk.entities.LANGUAGE_TABLE_NAME
import hu.bme.aut.android.podcasts.data.disk.entities.RoomLanguageItem

@Dao
interface LanguageDao {

    @Query("SELECT * FROM $LANGUAGE_TABLE_NAME")
    suspend fun getLanguages(): List<RoomLanguageItem>

    @Insert(onConflict = IGNORE)
    suspend fun insertLanguage(roomLanguageItem: RoomLanguageItem)

    @Transaction
    suspend fun insertAllLanguages(list: List<RoomLanguageItem>) {
        deleteAll()
        list.forEach { insertLanguage(it) }
    }

    @Query("DELETE FROM $LANGUAGE_TABLE_NAME WHERE `name` = :name")
    suspend fun deleteLanguage(name: String)

    @Query("DELETE FROM $LANGUAGE_TABLE_NAME")
    suspend fun deleteAll()

}