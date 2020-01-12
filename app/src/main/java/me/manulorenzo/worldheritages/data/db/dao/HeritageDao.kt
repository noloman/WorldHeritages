package me.manulorenzo.worldheritages.data.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.manulorenzo.worldheritages.data.db.entity.HeritageEntity

@Dao
interface HeritageDao {
    @Insert
    suspend fun insertAll(heritageEntityList: List<HeritageEntity>)

    @Query("SELECT * from heritages_table")
    fun getHeritageEntityDataSource(): DataSource.Factory<Int, HeritageEntity>

    @Query("SELECT COUNT(*) FROM heritages_table")
    suspend fun numberHeritagesInDb(): Long
}