package me.manulorenzo.worldheritages.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.manulorenzo.worldheritages.data.db.dao.HeritageDao
import me.manulorenzo.worldheritages.data.db.entity.HeritageEntity

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [HeritageEntity::class], version = 1, exportSchema = false)
abstract class HeritageDatabase : RoomDatabase() {
    abstract fun heritageDao(): HeritageDao

    companion object {
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
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}