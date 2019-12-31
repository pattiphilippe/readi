package patti.philippe.read_i.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.google.firebase.auth.FirebaseUser

import kotlinx.android.synthetic.main.fragment_emailpassword.emailSignInButton
import kotlinx.android.synthetic.main.fragment_emailpassword.emailCreateAccountButton
import kotlinx.android.synthetic.main.fragment_emailpassword.fieldEmail
import kotlinx.android.synthetic.main.fragment_emailpassword.fieldPassword
import kotlinx.android.synthetic.main.fragment_emailpassword.sign_in_error

import patti.philippe.read_i.MainActivity
import patti.philippe.read_i.R
import patti.philippe.read_i.WelcomeActivity.Companion.RqC_SIGN_IN
import patti.philippe.read_i.WelcomeActivity.Companion.EXTRA_USER

class EmailPasswordFragment : BaseFragment(), View.OnClickListener {


    //TODO put logs in methods
    //TODO check progressBar

    companion object {
        private const val TAG = "EmailPassword"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_emailpassword, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProgressBar(R.id.progressBar)
        emailSignInButton.setOnClickListener(this)
        emailCreateAccountButton.setOnClickListener(this)
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        showProgressBar()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    signedIn(auth.currentUser!!)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    enableError(true, task.exception?.message)
                    Toast.makeText(
                        requireContext(), "Authentication failed.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                hideProgressBar()
            }
    }

    private fun signedIn(user: FirebaseUser) {
        context?.let {
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                putExtra(EXTRA_USER, user)
            }
            startActivityForResult(intent, RqC_SIGN_IN)
        }
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        showProgressBar()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    signedIn(auth.currentUser!!)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    enableError(true, task.exception?.message)
                    Toast.makeText(
                        requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                hideProgressBar()
            }
    }
    //TODO add profile page in main activity, with possibility to verify email


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

    private fun enableError(enable: Boolean, error: String? = null) {
        sign_in_error.isEnabled = enable
        if (enable) {
            sign_in_error.visibility = View.VISIBLE
        } else {
            sign_in_error.visibility = View.GONE
        }
        error?.let {
            sign_in_error.text = resources.getString(R.string.sign_in_error, error)
        }
    }

    override fun onStop() {
        super.onStop()
        enableError(false)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.emailCreateAccountButton -> createAccount(
                fieldEmail.text.toString(),
                fieldPassword.text.toString()
            )
            R.id.emailSignInButton -> signIn(
                fieldEmail.text.toString(),
                fieldPassword.text.toString()
            )
        }
    }

}


