package me.manulorenzo.worldheritages

import android.database.Cursor
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.room.RoomDatabase
import androidx.room.RoomSQLiteQuery
import androidx.room.paging.LimitOffsetDataSource
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Since Kotlin has reified types, we can take advantage of it and test the type of the instance
 */
inline fun <reified T> tryCast(instance: Any?, block: T.() -> Unit) {
    if (instance is T) block(instance)
}

fun <T> List<T>.asPagedList(config: PagedList.Config? = null): PagedList<T>? {
    val defaultConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(size)
        .setMaxSize(size + 2)
        .setPrefetchDistance(1)
        .build()
    return LivePagedListBuilder<Int, T>(
        createMockDataSourceFactory(this),
        config ?: defaultConfig
    ).build().blockingObserve()
}

fun <T> LiveData<T>.blockingObserve(): T? {
    var value: T? = null
    val latch = CountDownLatch(1)

    val observer = Observer<T> { t ->
        value = t
        latch.countDown()
    }

    observeForever(observer)

    latch.await(2, TimeUnit.SECONDS)
    return value
}

private val mockedQuery: RoomSQLiteQuery = mock {
    on { sql } doReturn ""
}

private val mockedDb: RoomDatabase = mock {
    on { invalidationTracker } doReturn mock()
}

fun <T> createMockDataSourceFactory(itemList: List<T>): DataSource.Factory<Int, T> =
    object : DataSource.Factory<Int, T>() {
        override fun create(): DataSource<Int, T> = MockLimitDataSource(itemList)
    }

class MockLimitDataSource<T>(@VisibleForTesting val itemList: List<T>) :
    LimitOffsetDataSource<T>(mockedDb, mockedQuery, false, null) {
    override fun convertRows(cursor: Cursor?): MutableList<T> = itemList.toMutableList()
    override fun countItems(): Int = itemList.count()
    override fun isInvalid(): Boolean = false
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
    }

    override fun loadRange(startPosition: Int, loadCount: Int): MutableList<T> =
        itemList.subList(startPosition, startPosition + loadCount).toMutableList()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {}
}