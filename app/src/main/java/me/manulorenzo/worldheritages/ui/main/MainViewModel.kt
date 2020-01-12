package me.manulorenzo.worldheritages.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.manulorenzo.worldheritages.data.HeritageBoundaryCallback
import me.manulorenzo.worldheritages.data.model.Heritage
import me.manulorenzo.worldheritages.data.source.Repository

class MainViewModel(
    coroutinesIoDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val repository: Repository
) : ViewModel() {
    val worldHeritagesLiveData: LiveData<PagedList<Heritage>> =
        liveData(context = Dispatchers.Main) {
            LivePagedListBuilder(
                repository.fetchHeritagesList(),
                20
            ).setBoundaryCallback(HeritageBoundaryCallback(repository, viewModelScope))
                .build()
        }
}