package patti.philippe.read_i

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseUser

class MainViewModel(application: Application) : AndroidViewModel(application){
    var currentUser: FirebaseUser? = null
}