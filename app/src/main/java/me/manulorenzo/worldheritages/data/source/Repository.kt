package me.manulorenzo.worldheritages.data.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.squareup.moshi.JsonDataException
import me.manulorenzo.worldheritages.data.ParserManager
import me.manulorenzo.worldheritages.data.Resource
import me.manulorenzo.worldheritages.data.db.dao.HeritageDao
import me.manulorenzo.worldheritages.data.db.entity.toModel
import me.manulorenzo.worldheritages.data.model.Heritage
import java.io.IOException

typealias HeritageResponse = LiveData<PagedList<Heritage?>?>

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