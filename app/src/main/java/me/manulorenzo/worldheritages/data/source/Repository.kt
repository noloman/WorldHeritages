package me.manulorenzo.worldheritages.data.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import me.manulorenzo.worldheritages.data.ParserManager
import me.manulorenzo.worldheritages.data.db.dao.HeritageDao
import me.manulorenzo.worldheritages.data.db.entity.toModel
import me.manulorenzo.worldheritages.data.model.Heritage

typealias HeritageResponse = LiveData<PagedList<Heritage?>?>

@Mockable
class Repository(
    private val parserManager: ParserManager,
    private val heritageDao: HeritageDao
) {
    suspend fun fetchHeritagesList(
        startPosition: Int = 0,
        pageSize: Int = DATABASE_PAGE_SIZE
    ): DataSource.Factory<Int, Heritage> {
        val rowsCount = heritageDao.numberHeritagesInDb()
        if (rowsCount == 0L) heritageDao.insertAll(parserManager.parseAssetsToEntityList())
        return heritageDao.getHeritageEntityDataSource().map { it.toModel() }
    }

    companion object {
        const val DEFAULT_ERROR_MSG = "Error"
        private const val DATABASE_PAGE_SIZE = 20
    }
}