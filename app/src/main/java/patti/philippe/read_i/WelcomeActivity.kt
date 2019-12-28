package patti.philippe.read_i

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseUser
import patti.philippe.read_i.auth.EmailPasswordFragment

class WelcomeActivity : AppCompatActivity() {

    private lateinit var viewModel : WelcomeViewModel

    companion object{
        private const val RC = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(WelcomeViewModel::class.java)
        val currentUser = viewModel.auth.currentUser
        if(currentUser != null && !currentUser.isAnonymous) {
            signedIn(currentUser)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

    }

    private fun signedIn(user: FirebaseUser) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(EmailPasswordFragment.EXTRA_USER, user)
        }
        startActivityForResult(intent, RC)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RC -> signOut()
        }
    }

    private fun signOut(){
        viewModel.auth.signOut()
        println("signed out and current user email is ${viewModel.auth.currentUser?.email}")
    }

}