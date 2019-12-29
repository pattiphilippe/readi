package patti.philippe.read_i

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    companion object{
         const val RC_SIGN_IN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode){
            MainActivity.RC_SIGN_OUT -> {
                signOut()
            }
        }
    }

    private fun signOut(){
        auth.signOut()
    }

    override fun onDestroy() {
        signOut()
        super.onDestroy()
    }

}