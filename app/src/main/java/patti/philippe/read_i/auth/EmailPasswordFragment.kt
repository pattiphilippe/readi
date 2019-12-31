package patti.philippe.read_i.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.fragment_emailpassword.emailSignInButton
import kotlinx.android.synthetic.main.fragment_emailpassword.emailCreateAccountButton
import kotlinx.android.synthetic.main.fragment_emailpassword.fieldEmail
import kotlinx.android.synthetic.main.fragment_emailpassword.fieldPassword

import patti.philippe.read_i.R

class EmailPasswordFragment : BaseFragment() {


    override val TAG: String = "EmailPassword"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_emailpassword, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setError(R.id.sign_in_error)
    }


    override fun initListeners() {
        emailSignInButton.setOnClickListener(this)
        emailCreateAccountButton.setOnClickListener(this)
    }

    override fun signIn() {
        super.signIn()

        val email = fieldEmail.text.toString()
        val password = fieldPassword.text.toString()

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
                    showError(task.exception!!)
                }
                hideProgressBar()
            }
    }

    private fun createAccount() {
        val email = fieldEmail.text.toString()
        val password = fieldPassword.text.toString()

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
                    showError(task.exception!!)
                }
                hideProgressBar()
            }
    }


    private fun validateForm(): Boolean {
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            Log.w(TAG, "Email required!")
            valid = false
        } else {
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = "Required."
            Log.w(TAG, "Password required!")
            valid = false
        } else {
            fieldPassword.error = null
        }

        return valid
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.emailCreateAccountButton -> createAccount()
            R.id.emailSignInButton -> signIn()
        }
    }

}


