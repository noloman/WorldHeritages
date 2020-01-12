package me.manulorenzo.worldheritages.data.di

import me.manulorenzo.worldheritages.WorldHeritagesApplication
import org.koin.core.module.Module
import org.koin.dsl.module

fun assetManagerModule(worldHeritagesApplication: WorldHeritagesApplication): Module = module {
    single {
        worldHeritagesApplication.assets
    }
}