package me.manulorenzo.worldheritages

import android.app.Application
import me.manulorenzo.worldheritages.data.di.assetManagerModule
import me.manulorenzo.worldheritages.data.di.repositoryModule
import me.manulorenzo.worldheritages.ui.main.di.mainViewModelModule
import me.manulorenzo.worldheritages.ui.main.viewModuleFactoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WorldHeritagesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@WorldHeritagesApplication)
            modules(
                listOf(
                    repositoryModule,
                    mainViewModelModule,
                    viewModuleFactoryModule,
                    assetManagerModule(this@WorldHeritagesApplication)
                )
            )
        }
    }
}