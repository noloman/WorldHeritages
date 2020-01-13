package me.manulorenzo.worldheritages.ui.main.di

import me.manulorenzo.worldheritages.ui.main.ViewModelFactory
import org.koin.dsl.module

val viewModuleFactoryModule = module {
    single {
        ViewModelFactory(
            get()
        )
    }
}