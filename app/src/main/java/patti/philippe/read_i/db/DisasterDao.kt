package patti.philippe.read_i.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DisasterDao{

     @Query("SELECT * FROM disaster_table")
     fun getDisasters() : LiveData<List<Disaster>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(disaster: Disaster)

    @Query("DELETE FROM disaster_table")
    suspend fun deleteAll()
}