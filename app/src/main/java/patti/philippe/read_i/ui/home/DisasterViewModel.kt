package patti.philippe.read_i.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import patti.philippe.read_i.db.Disaster
import patti.philippe.read_i.db.DisasterRepository
import patti.philippe.read_i.db.DisasterRoomDatabase

class DisasterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DisasterRepository
    val allDisasters: LiveData<List<Disaster>>

    init {
        val disastersDao =
            DisasterRoomDatabase.getDatabase(application, viewModelScope).disasterDao()
        repository = DisasterRepository(disastersDao)
        allDisasters = repository.allDisasters
    }

}