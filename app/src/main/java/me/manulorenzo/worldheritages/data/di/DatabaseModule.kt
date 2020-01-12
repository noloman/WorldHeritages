package me.manulorenzo.worldheritages.data.di

import me.manulorenzo.worldheritages.data.db.HeritageDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { HeritageDatabase.getDatabase(get()) }
    single { get<HeritageDatabase>().heritageDao() }
}