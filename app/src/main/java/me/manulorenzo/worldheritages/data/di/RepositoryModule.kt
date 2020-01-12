package me.manulorenzo.worldheritages.data.di

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.manulorenzo.worldheritages.data.ParserManager
import me.manulorenzo.worldheritages.data.source.Repository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        Repository(
            parserManager = get(),
            heritageDao = get()
        )
    }
}
val coroutineScopeModule = module {
    factory { SupervisorJob() }
    factory { CoroutineScope(Dispatchers.IO + get<CompletableJob>()) }
}
val parserManagerModule = module { single { ParserManager(assetsManager = get()) } }