package me.manulorenzo.worldheritages.ui.main.di

import me.manulorenzo.worldheritages.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainViewModelModule = module { viewModel { MainViewModel(repository = get()) } }