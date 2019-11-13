package patti.philippe.read_i.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Disaster::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DisasterRoomDatabase : RoomDatabase() {

    abstract fun disasterDao(): DisasterDao

    private class DisasterDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.disasterDao())
                }
            }
        }

        suspend fun populateDatabase(disasterDao: DisasterDao) {
            disasterDao.deleteAll()
            DisasterType.values().forEach { type ->
                disasterDao.insert(Disaster(type))
            }

        }
    }

    companion object {

        @Volatile
        private var INSTANCE: DisasterRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): DisasterRoomDatabase {
            val tempInstance = INSTANCE
            tempInstance?.let { return tempInstance }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DisasterRoomDatabase::class.java,
                    "disaster_database"
                ).addCallback(DisasterDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}