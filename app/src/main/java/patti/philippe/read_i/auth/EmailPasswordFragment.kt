package patti.philippe.read_i.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.fragment_emailpassword.*
import patti.philippe.read_i.R

class EmailPasswordFragment : BaseFragment(R.layout.fragment_emailpassword) {


    override val mTag: String = "EmailPassword"


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

        Log.d(mTag, "signIn:$email")

        if (!validateForm()) {
            return
        }

        showProgressBar()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(mTag, "signInWithEmail:success")
                    signedIn(auth.currentUser!!)
                } else {
                    Log.w(mTag, "signInWithEmail:failure", task.exception)
                    showError(task.exception!!)
                }
                hideProgressBar()
            }
    }

    private fun createAccount() {
        val email = fieldEmail.text.toString()
        val password = fieldPassword.text.toString()

        Log.d(mTag, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        showProgressBar()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(mTag, "createUserWithEmail:success")
                    signedIn(auth.currentUser!!)
                } else {
                    Log.w(mTag, "createUserWithEmail:failure", task.exception)
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
            Log.w(mTag, "Email required!")
            valid = false
        } else {
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = "Required."
            Log.w(mTag, "Password required!")
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


