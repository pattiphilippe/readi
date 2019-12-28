package patti.philippe.read_i

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth

class WelcomeViewModel(application: Application) : AndroidViewModel(application){
    val auth = FirebaseAuth.getInstance()

}