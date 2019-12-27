package patti.philippe.read_i.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_emailpassword.emailCreateAccountButton
import kotlinx.android.synthetic.main.activity_emailpassword.emailSignInButton
import kotlinx.android.synthetic.main.activity_emailpassword.fieldEmail
import kotlinx.android.synthetic.main.activity_emailpassword.fieldPassword
import kotlinx.android.synthetic.main.activity_emailpassword.sign_in_error
import patti.philippe.read_i.MainActivity
import patti.philippe.read_i.R

class EmailPasswordActivity : BaseActivity(), View.OnClickListener {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    companion object {
        val EXTRA_USER = "EXTRA_USER"
        private const val TAG = "EmailPassword"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val currentUser = auth.currentUser
        currentUser?.let {
            signedIn(currentUser)
        }

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_emailpassword)
        setProgressBar(R.id.progressBar)

        // Buttons
        emailSignInButton.setOnClickListener(this)
        emailCreateAccountButton.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()

        if(intent.hasExtra(MainActivity.EXTRA_SIGN_OUT)){
            signOut()
        }
    }

    private fun signedIn(user: FirebaseUser){
        val intent = Intent(this, MainActivity::class.java).apply{
            putExtra(EXTRA_USER, user)
        }
        startActivity(intent)
    }

    private fun signOut() {
        auth.signOut()
        hideProgressBar()
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        showProgressBar()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    signedIn(auth.currentUser!!)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    enableError(true, task.exception?.message)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
                hideProgressBar()
            }
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        showProgressBar()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    signedIn(auth.currentUser!!)
                } else {
                    //TODO explain error
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    enableError(true, task.exception?.message)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_LONG).show()
                }
                hideProgressBar()
            }
    }

    private fun sendEmailVerification() {
        //TODO add profile page in main activity, with possibility to verify email
        // Disable button

        // Send verification email
        // [START send_email_verification]
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                // [START_EXCLUDE]
                // Re-enable button

                if (task.isSuccessful) {
                    Toast.makeText(baseContext,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
                // [END_EXCLUDE]
            }
        // [END send_email_verification]
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            valid = false
        } else {
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = "Required."
            valid = false
        } else {
            fieldPassword.error = null
        }

        return valid
    }

    private fun enableError(enable : Boolean, error : String? = null){
        sign_in_error.isEnabled = enable
        if (enable){
            sign_in_error.visibility = View.VISIBLE
        } else {
            sign_in_error.visibility = View.GONE
        }
        error?.let {
            sign_in_error.text = error
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.emailCreateAccountButton -> createAccount(fieldEmail.text.toString(), fieldPassword.text.toString())
            R.id.emailSignInButton -> signIn(fieldEmail.text.toString(), fieldPassword.text.toString())
        }
    }

}


