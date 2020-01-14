package me.manulorenzo.worldheritages.domain

import androidx.paging.PagedList
import me.manulorenzo.worldheritages.domain.model.Heritage
import me.manulorenzo.worldheritages.domain.repository.Repository

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