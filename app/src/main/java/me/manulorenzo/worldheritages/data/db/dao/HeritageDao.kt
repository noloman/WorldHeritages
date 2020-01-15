package me.manulorenzo.worldheritages.data.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.manulorenzo.worldheritages.data.model.HeritageEntity

@Dao
interface HeritageDao {
    @Insert
    fun insertAll(heritageEntityList: List<HeritageEntity>)

    @Query("SELECT * from heritages_table")
    fun getHeritageEntityDataSource(): DataSource.Factory<Int, HeritageEntity>
}