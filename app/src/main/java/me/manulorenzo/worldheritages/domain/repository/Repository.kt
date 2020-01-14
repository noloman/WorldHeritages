package me.manulorenzo.worldheritages.domain.repository

import androidx.paging.DataSource
import com.squareup.moshi.JsonDataException
import me.manulorenzo.worldheritages.data.db.dao.HeritageDao
import me.manulorenzo.worldheritages.data.model.toModel
import me.manulorenzo.worldheritages.domain.ParserManager
import me.manulorenzo.worldheritages.domain.model.Heritage
import me.manulorenzo.worldheritages.domain.model.Resource
import java.io.IOException

@Mockable
class Repository(
    private val parserManager: ParserManager,
    private val heritageDao: HeritageDao
) {
    fun fetchHeritagesList(
    ): Resource<DataSource.Factory<Int, Heritage>> = try {
        Resource.Success(heritageDao.getHeritageEntityDataSource().map { it.toModel() })
    } catch (e: JsonDataException) {
        Resource.Error(DEFAULT_ERROR_MESSAGE)
    } catch (e: IOException) {
        Resource.Error(DEFAULT_ERROR_MESSAGE)
    }

    suspend fun insertAllHeritages() {
        heritageDao.insertAll(parserManager.parseAssetsToEntityList())
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "There was an unexpected error"
    }
}