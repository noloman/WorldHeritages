package me.manulorenzo.worldheritages.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.manulorenzo.worldheritages.data.Resource
import me.manulorenzo.worldheritages.data.source.HeritageResponse
import me.manulorenzo.worldheritages.data.source.Repository

class MainViewModel(
    coroutinesIoDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val repository: Repository
) : ViewModel() {
    val worldHeritagesLiveData: LiveData<HeritageResponse> =
        liveData(context = viewModelScope.coroutineContext + coroutinesIoDispatcher) {
            emit(Resource.Loading())
            emit(repository.fetchHeritagesList())
        }
}