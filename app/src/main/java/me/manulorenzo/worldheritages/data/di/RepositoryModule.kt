package me.manulorenzo.worldheritages.data.di

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
val parserManagerModule = module { single { ParserManager(assetsManager = get()) } }