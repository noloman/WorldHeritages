package me.manulorenzo.worldheritages.data

import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.manulorenzo.worldheritages.data.model.Heritage
import java.io.IOException

typealias HeritageResponse = Resource<List<Heritage?>?>

class Repository(
    private val assetManager: AssetManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun fetchHeritagesList(): HeritageResponse = withContext(ioDispatcher) {
        try {
            // TODO Blocking method call?
            val bufferReader = assetManager.open("heritages.json").bufferedReader()
            val data = bufferReader.use {
                it.readText()
            }
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val listType = Types.newParameterizedType(List::class.java, Heritage::class.java)
            val adapter: JsonAdapter<List<Heritage>> = moshi.adapter(listType)
            // TODO Blocking method call?
            val result: List<Heritage>? = adapter.fromJson(data)
            // TODO Error checking
            Resource.Success<List<Heritage?>?>(result)
        } catch (e: JsonDataException) {
            Resource.Error<List<Heritage?>?>("Error")
        } catch (e: IOException) {
            Resource.Error<List<Heritage?>?>("Error")
        }
    }
}