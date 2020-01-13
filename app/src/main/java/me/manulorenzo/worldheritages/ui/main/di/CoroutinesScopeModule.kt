package me.manulorenzo.worldheritages.ui.main.di

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val coroutineScopeModule = module {
    factory { SupervisorJob() }
    factory { CoroutineScope(Dispatchers.IO + get<CompletableJob>()) }
}