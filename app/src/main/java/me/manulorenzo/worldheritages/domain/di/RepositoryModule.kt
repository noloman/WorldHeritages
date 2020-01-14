package me.manulorenzo.worldheritages.domain.di

import me.manulorenzo.worldheritages.domain.ParserManager
import me.manulorenzo.worldheritages.domain.repository.Repository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        Repository(
            parserManager = get(),
            heritageDao = get()
        )
    }
}
val parserManagerModule = module {
    single {
        ParserManager(
            assetsManager = get()
        )
    }
}