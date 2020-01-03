package patti.philippe.read_i.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth


class ProfileViewModel(application: Application)
    : AndroidViewModel(application){
    val currentUser = FirebaseAuth.getInstance().currentUser
}