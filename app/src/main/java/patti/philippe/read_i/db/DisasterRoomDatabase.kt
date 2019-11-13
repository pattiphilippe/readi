package patti.philippe.read_i.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(Disaster::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DisasterRoomDatabase : RoomDatabase() {

    abstract fun disasterDao() : DisasterDao

    companion object {

        @Volatile
        private var INSTANCE: DisasterRoomDatabase? = null

        fun getDatabase(context: Context): DisasterRoomDatabase {
            val tempInstance = INSTANCE
            tempInstance?.let { return tempInstance }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DisasterRoomDatabase::class.java,
                    "disaster_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}