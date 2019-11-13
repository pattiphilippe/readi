package patti.philippe.read_i.db

import androidx.lifecycle.LiveData

class DisasterRepository (private val disasterDao: DisasterDao){

    val allDisasters : LiveData<List<Disaster>> = disasterDao.getDisasters()

    suspend fun insert(disaster: Disaster) {
        disasterDao.insert(disaster)
    }


}