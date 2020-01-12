package me.manulorenzo.worldheritages.data.source

import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.manulorenzo.worldheritages.data.ParserManager
import me.manulorenzo.worldheritages.data.Resource
import me.manulorenzo.worldheritages.data.db.dao.HeritageDao
import me.manulorenzo.worldheritages.data.db.entity.toModel
import me.manulorenzo.worldheritages.data.model.Heritage
import java.io.IOException

typealias HeritageResponse = Resource<List<Heritage?>?>

@Mockable
class Repository(
    private val parserManager: ParserManager,
    private val heritageDao: HeritageDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun fetchHeritagesList(): HeritageResponse = withContext(ioDispatcher) {
        try {
            val number = heritageDao.numberHeritagesInDb()
            if (number == 0L) heritageDao.insertAll(parserManager.parseAssetsToEntityList())
            Resource.Success<List<Heritage?>?>(heritageDao.getHeritageList().map { it.toModel() })
        } catch (e: JsonDataException) {
            Resource.Error<List<Heritage?>?>(DEFAULT_ERROR_MSG)
        } catch (e: IOException) {
            Resource.Error<List<Heritage?>?>(DEFAULT_ERROR_MSG)
        }
    }

    companion object {
        const val DEFAULT_ERROR_MSG = "Error"
    }
}