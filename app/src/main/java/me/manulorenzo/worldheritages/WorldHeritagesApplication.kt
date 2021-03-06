package me.manulorenzo.worldheritages

import android.app.Application
import android.os.StrictMode
import me.manulorenzo.worldheritages.data.di.databaseModule
import me.manulorenzo.worldheritages.di.assetManagerModule
import me.manulorenzo.worldheritages.domain.di.parserManagerModule
import me.manulorenzo.worldheritages.domain.di.repositoryModule
import me.manulorenzo.worldheritages.ui.main.di.coroutineScopeModule
import me.manulorenzo.worldheritages.ui.main.di.mainViewModelModule
import me.manulorenzo.worldheritages.ui.main.di.viewModuleFactoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WorldHeritagesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
        startKoin {
            androidLogger()
            androidContext(this@WorldHeritagesApplication)
            modules(
                listOf(
                    repositoryModule,
                    mainViewModelModule,
                    viewModuleFactoryModule,
                    parserManagerModule,
                    databaseModule,
                    coroutineScopeModule,
                    assetManagerModule(this@WorldHeritagesApplication)
                )
            )
        }
    }
}