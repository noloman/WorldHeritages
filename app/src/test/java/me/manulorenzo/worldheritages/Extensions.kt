package me.manulorenzo.worldheritages

import android.database.Cursor
import androidx.annotation.VisibleForTesting
import androidx.paging.DataSource
import androidx.room.RoomDatabase
import androidx.room.RoomSQLiteQuery
import androidx.room.paging.LimitOffsetDataSource
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock

/**
 * Since Kotlin has reified types, we can take advantage of it and test the type of the instance
 */
inline fun <reified T> tryCast(instance: Any?, block: T.() -> Unit) {
    if (instance is T) block(instance)
}

private val mockedQuery = mock<RoomSQLiteQuery> {
    on { sql } doReturn ""
}

private val mockedDb = mock<RoomDatabase> {
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