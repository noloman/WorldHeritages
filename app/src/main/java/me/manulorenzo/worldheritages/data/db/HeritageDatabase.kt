package me.manulorenzo.worldheritages.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.manulorenzo.worldheritages.data.db.dao.HeritageDao
import me.manulorenzo.worldheritages.data.model.HeritageEntity
import me.manulorenzo.worldheritages.domain.repository.Repository
import org.koin.core.KoinComponent
import org.koin.core.inject

@Database(entities = [HeritageEntity::class], version = 1, exportSchema = false)
abstract class HeritageDatabase : RoomDatabase() {
    abstract fun heritageDao(): HeritageDao

    companion object : KoinComponent {
        val repository: Repository by inject()
        @Volatile
        private var INSTANCE: HeritageDatabase? = null

        fun getDatabase(context: Context): HeritageDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HeritageDatabase::class.java,
                    "heritages_database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        GlobalScope.launch {
                            withContext(Dispatchers.IO) {
                                repository.insertAllHeritages()
                            }
                        }
                    }
                })
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}