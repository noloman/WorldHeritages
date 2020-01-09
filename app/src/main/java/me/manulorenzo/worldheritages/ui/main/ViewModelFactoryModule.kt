package me.manulorenzo.worldheritages.ui.main

import org.koin.dsl.module

val viewModuleFactoryModule = module { single { ViewModelFactory(get()) } }