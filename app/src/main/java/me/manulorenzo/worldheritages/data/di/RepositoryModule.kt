package me.manulorenzo.worldheritages.data.di

import me.manulorenzo.worldheritages.WorldHeritagesApplication
import me.manulorenzo.worldheritages.data.Repository
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule = module { single { Repository(assetManager = get()) } }

fun assetManagerModule(worldHeritagesApplication: WorldHeritagesApplication): Module = module {
    single {
        worldHeritagesApplication.assets
    }
}