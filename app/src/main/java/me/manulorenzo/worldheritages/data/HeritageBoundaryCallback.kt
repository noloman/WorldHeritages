package me.manulorenzo.worldheritages.data

import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.manulorenzo.worldheritages.data.model.Heritage
import me.manulorenzo.worldheritages.data.source.Repository

class HeritageBoundaryCallback(
    private val repository: Repository,
    private val scope: CoroutineScope
) : PagedList.BoundaryCallback<Heritage>() {
    private var lastRequestedPage = 0

    override fun onZeroItemsLoaded() {
        scope.launch {
            requestData()
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Heritage) {
        scope.launch {
            requestData()
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: Heritage) {
        scope.launch {
            requestData()
        }
    }

    private suspend fun requestData() {
        repository.fetchHeritagesList(
            startPosition = lastRequestedPage++,
            pageSize = 20
        )
    }
}