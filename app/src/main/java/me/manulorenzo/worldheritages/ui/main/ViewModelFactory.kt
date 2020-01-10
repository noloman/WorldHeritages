package me.manulorenzo.worldheritages.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.manulorenzo.worldheritages.data.source.Repository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                repository = repository
            ) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}