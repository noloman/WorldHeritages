package me.manulorenzo.worldheritages.data

import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.manulorenzo.worldheritages.data.db.entity.HeritageEntity
import me.manulorenzo.worldheritages.data.model.Heritage
import me.manulorenzo.worldheritages.data.model.toEntity

/**
 * Ideally we should inject Moshi here via constructor param, so it could be testable and make this class more SRP.
 * Unfortunately I don't have time to fight with Moshi and Koin now
 */
class ParserManager(
    private val assetsManager: AssetManager,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun parseAssetsToEntityList(): List<HeritageEntity> = withContext(defaultDispatcher) {
        val bufferReader = assetsManager.open("heritages.json").bufferedReader()
        val data = bufferReader.use { it.readText() }
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val listType = Types.newParameterizedType(List::class.java, Heritage::class.java)
        val adapter: JsonAdapter<List<Heritage>> = moshi.adapter(listType)
        val result = adapter.fromJson(data)?.map { it.toEntity() }
        return@withContext result ?: emptyList()
    }
}