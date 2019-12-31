package patti.philippe.read_i

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    companion object {
        const val EXTRA_USER = "EXTRA_USER"
        const val RqC_SIGN_IN = 100
        const val RsC_SIGN_OUT = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RsC_SIGN_OUT -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        auth.signOut()
    }

    override fun onDestroy() {
        signOut()
        super.onDestroy()
    }

}