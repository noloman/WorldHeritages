package me.manulorenzo.worldheritages.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import me.manulorenzo.worldheritages.domain.HeritageBoundaryCallback
import me.manulorenzo.worldheritages.domain.model.Heritage
import me.manulorenzo.worldheritages.domain.model.Resource
import me.manulorenzo.worldheritages.domain.repository.Repository

class MainViewModel(repository: Repository) : ViewModel() {
    val errorPagedList = MutableLiveData<Boolean>()
    val worldHeritagesLiveData: LiveData<PagedList<Heritage>>? =
        when (val heritagesList: Resource<DataSource.Factory<Int, Heritage>> =
            repository.fetchHeritagesList()) {
            is Resource.Success ->
                heritagesList.data?.let {
                    LivePagedListBuilder(
                        it,
                        PagedList.Config.Builder()
                            .setEnablePlaceholders(true)
                            .setPageSize(PAGE_SIZE)
                            .build()
                    ).setBoundaryCallback(
                        HeritageBoundaryCallback(
                            repository
                        )
                    )
                        .build()
                }
            is Resource.Error -> {
                Log.e("Error", "There was an error loading the heritages")
                errorPagedList.postValue(true)
                null
            }
        }


    companion object {
        const val PAGE_SIZE = 20
    }
}