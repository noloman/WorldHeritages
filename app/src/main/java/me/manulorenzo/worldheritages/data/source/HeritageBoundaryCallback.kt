package me.manulorenzo.worldheritages.data.source

import androidx.paging.PagedList
import me.manulorenzo.worldheritages.data.model.Heritage

class HeritageBoundaryCallback(
    private val repository: Repository
) : PagedList.BoundaryCallback<Heritage>() {
    override fun onZeroItemsLoaded() {
        requestData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Heritage) {
        requestData()
    }

    override fun onItemAtFrontLoaded(itemAtFront: Heritage) {
        requestData()
    }

    private fun requestData() {
        repository.fetchHeritagesList()
    }
}